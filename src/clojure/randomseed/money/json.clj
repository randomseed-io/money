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

(ns randomseed.money.json
  "Provides Cheshire integration.

   org.joda.money.Money instances are serialized to strings with human-readable
   amounts following ISO-4217 currency codes.

   Currency units are serialized to strings by taking their ISO-4217 codes."
  (:require cheshire.generate
            [randomseed.money.amounts :as ma])
  (:import [org.joda.money Money CurrencyUnit]
           [com.fasterxml.jackson.core.json WriterBasedJsonGenerator]))

(defn- encode-money
  "Encodes an instance of org.joda.money.Money as a string with an ISO-4217
  currency code followed by a human-readable amount."
  ^String
  [^Money m]
  (format "%s %s" (.getCode (ma/currency-of m)) (.getAmount m)))

(cheshire.generate/add-encoder CurrencyUnit
                               (fn [^CurrencyUnit cu ^WriterBasedJsonGenerator generator]
                                 (.writeString generator (.getCode cu))))

(cheshire.generate/add-encoder Money
                               (fn [^Money m ^WriterBasedJsonGenerator generator]
                                 (.writeString generator (encode-money m))))
