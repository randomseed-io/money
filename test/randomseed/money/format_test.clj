(ns randomseed.money.format-test
  (:require [randomseed.money.amounts    :as ams]
            [randomseed.money.currencies :as cu]
            [randomseed.money.format     :as fmt]
            [trptr.java-wrapper.locale   :as l])
  (:use clojure.test)
  (:import java.util.Locale))


(deftest test-formatting-with-default-formatter-and-provided-locale
  (are [formatted money] (is (= formatted (fmt/format money (Locale/UK))))
       "£10.00"   (ams/amount-of cu/GBP 10.00))
  (are [formatted money] (is (= formatted (fmt/format money (l/locale :pl))))
       "€10,00"   (ams/amount-of cu/EUR 10.00)
       "GBP10,00" (ams/amount-of cu/GBP 10.00)
       "USD10,00" (ams/amount-of cu/USD 10.00)))
