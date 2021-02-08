;; This source code is dual-licensed under the Apache License, version
;; 2.0, and the Eclipse Public License, version 1.0.
;;
;; The APL v2.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2021 Paweł Wilk and the random:seed team
;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
;; ----------------------------------------------------------------------------------
;;
;; The EPL v1.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2021 Paweł Wilk and the random:seed team
;; Copyright (c) 2012-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team.
;; All rights reserved.
;;
;; This program and the accompanying materials are made available under the terms of
;; the Eclipse Public License Version 1.0,
;; which accompanies this distribution and is available at
;; http://www.eclipse.org/legal/epl-v10.html.
;; ----------------------------------------------------------------------------------

(ns randomseed.money.currencies
  "Provides functions that return currency units by their ISO 4217 codes and country
   codes.

   Includes var aliases for org.joda.money.CurrencyUnit constants, for example,
   randomseed.money.currencies/USD or randomseed.money.currencies/GBP."

  (:require [randomseed.money.util :as util])
  (:import org.joda.money.CurrencyUnit
           org.joda.money.DefaultCurrencyUnitDataProvider))

;;
;; Cryptocurrencies
;;

(defrecord CryptoInfo [^String code ^String name ^int decimal-places])

(defn- ^CurrencyUnit register-crypto!
  "Registers cryptocurrency. Returns CurrencyUnit object. Overwrites previously defined
  currency if it exists!"
  [^CryptoInfo ci]
  (CurrencyUnit/registerCurrency (.code ci) -1 (.decimal-places ci) true))

(defn- cryptoseq
  "Reads CSV data describing cryptocurrencies and generates a lazy sequence of
  CryptoInfo records."
  []
  (some->> "META-INF/io/randomseed/money/CryptoData.csv"
           util/resource-pathname
           util/read-csv
           (map (fn [[code decplaces name]]
                  (when (and (= 3 (count code)) (util/integer-string? decplaces))
                    (->CryptoInfo code name (Integer/parseInt decplaces)))))
           (remove nil?)))

(def ^clojure.lang.PersistentHashMap crypto
  "Map of registered cryptocurrencies (CurrencyUnit objects) and associated CryptoInfo
  records containing some extra data."
  (->> (cryptoseq)
       (map (juxt register-crypto! identity))
       (into {})))

;;
;; API
;;

(defn ^CurrencyUnit of
  "Returns currency unit for the specified ISO-4217 three letter currency code"
  [^String code]
  (CurrencyUnit/of code))

(defn ^CurrencyUnit for-code
  "Returns currency unit for the specified ISO-4217 three letter currency code"
  [^String code]
  (CurrencyUnit/of code))

(defn ^CurrencyUnit of-numeric-code
  "Returns currency unit for the specified numeric currency code"
  [^long code]
  (CurrencyUnit/ofNumericCode code))

(defn ^CurrencyUnit for-numeric-code
  "Returns currency unit for the specified numeric currency code"
  [^long code]
  (CurrencyUnit/ofNumericCode code))

(defn ^CurrencyUnit of-country
  "Returns currency unit for the specified country code. This
   function is case sensitive."
  [^String code]
  (CurrencyUnit/ofCountry code))

(defn ^CurrencyUnit for-country
  "Returns currency unit for the specified country code. This
   function is case sensitive."
  [^String code]
  (CurrencyUnit/ofCountry code))

(defn crypto-currency?
  "Returns true if this currency is a cryptocurrency."
  [^CurrencyUnit cu]
  (contains? crypto cu))

(defn pseudo-currency?
  "Returns true if this currency is a pseudo currency."
  [^CurrencyUnit cu]
  (.isPseudoCurrency cu))

(defprotocol ToCurrencyUnit
  (to-currency-unit [input] "Converts input to a CurrencyUnit instance"))

(extend-protocol ToCurrencyUnit
  String
  (to-currency-unit [^String s]
    (of s))

  CurrencyUnit
  (to-currency-unit [^CurrencyUnit cu]
    cu))


(defn ^java.util.List registered-currencies
  "Returns a list of registered currency units"
  []
  (CurrencyUnit/registeredCurrencies))

(defn code-of
  "Returns the currency code of the given currency"
  [^CurrencyUnit cu]
  (when cu
    (.getCode cu)))

(defmacro defalias
  [^String s]
  `(def ~(symbol s) (CurrencyUnit/of (String/valueOf ~s))))

;; Code generated via reflection.
;; This can be macro-defined using reflection, too.
(defalias "AED")
(defalias "AFN")
(defalias "ALL")
(defalias "AMD")
(defalias "ANG")
(defalias "AOA")
(defalias "ARS")
(defalias "AUD")
(defalias "AWG")
(defalias "AZN")
(defalias "BAM")
(defalias "BBD")
(defalias "BDT")
(defalias "BGN")
(defalias "BHD")
(defalias "BIF")
(defalias "BMD")
(defalias "BND")
(defalias "BOB")
(defalias "BRL")
(defalias "BSD")
(defalias "BTN")
(defalias "BWP")
(defalias "BYR")
(defalias "BZD")
(defalias "CAD")
(defalias "CDF")
(defalias "CHF")
(defalias "CLP")
(defalias "CNY")
(defalias "COP")
(defalias "CRC")
(defalias "CUP")
(defalias "CVE")
(defalias "CZK")
(defalias "DJF")
(defalias "DKK")
(defalias "DOP")
(defalias "DZD")
(defalias "EGP")
(defalias "ERN")
(defalias "ETB")
(defalias "EUR")
(defalias "FJD")
(defalias "FKP")
(defalias "GBP")
(defalias "GEL")
(defalias "GHS")
(defalias "GIP")
(defalias "GMD")
(defalias "GNF")
(defalias "GTQ")
(defalias "GYD")
(defalias "HKD")
(defalias "HNL")
(defalias "HRK")
(defalias "HTG")
(defalias "HUF")
(defalias "IDR")
(defalias "ILS")
(defalias "INR")
(defalias "IQD")
(defalias "IRR")
(defalias "ISK")
(defalias "JMD")
(defalias "JOD")
(defalias "JPY")
(defalias "KES")
(defalias "KGS")
(defalias "KHR")
(defalias "KMF")
(defalias "KPW")
(defalias "KRW")
(defalias "KWD")
(defalias "KYD")
(defalias "KZT")
(defalias "LAK")
(defalias "LBP")
(defalias "LKR")
(defalias "LRD")
(defalias "LSL")
(defalias "LTL")
(defalias "LVL")
(defalias "LYD")
(defalias "MAD")
(defalias "MDL")
(defalias "MGA")
(defalias "MKD")
(defalias "MMK")
(defalias "MNT")
(defalias "MOP")
(defalias "MRO")
(defalias "MUR")
(defalias "MVR")
(defalias "MWK")
(defalias "MXN")
(defalias "MYR")
(defalias "MZN")
(defalias "NAD")
(defalias "NGN")
(defalias "NIO")
(defalias "NOK")
(defalias "NPR")
(defalias "NZD")
(defalias "OMR")
(defalias "PAB")
(defalias "PEN")
(defalias "PGK")
(defalias "PHP")
(defalias "PKR")
(defalias "PLN")
(defalias "PYG")
(defalias "QAR")
(defalias "RON")
(defalias "RSD")
(defalias "RUB")
(defalias "RUR")
(defalias "RWF")
(defalias "SAR")
(defalias "SBD")
(defalias "SCR")
(defalias "SDG")
(defalias "SEK")
(defalias "SGD")
(defalias "SHP")
(defalias "SLL")
(defalias "SOS")
(defalias "SRD")
(defalias "STD")
(defalias "SYP")
(defalias "SZL")
(defalias "THB")
(defalias "TJS")
(defalias "TMT")
(defalias "TND")
(defalias "TOP")
(defalias "TRY")
(defalias "TTD")
(defalias "TWD")
(defalias "TZS")
(defalias "UAH")
(defalias "UGX")
(defalias "USD")
(defalias "UYU")
(defalias "UZS")
(defalias "VEF")
(defalias "VND")
(defalias "VUV")
(defalias "WST")
(defalias "XAF")
(defalias "XAG")
(defalias "XAU")
(defalias "XBA")
(defalias "XBB")
(defalias "XBC")
(defalias "XBD")
(defalias "XCD")
(defalias "XDR")
(defalias "XFU")
(defalias "XOF")
(defalias "XPD")
(defalias "XPF")
(defalias "XPT")
(defalias "XTS")
(defalias "XXX")
(defalias "YER")
(defalias "ZAR")
(defalias "ZWL")

;; Handle crypto.

(doseq [cc (keys crypto)]
  (intern *ns* (symbol (.getCode cc)) cc))
