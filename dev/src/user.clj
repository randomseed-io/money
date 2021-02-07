(ns user
  (:require
   [clojure.java.io]
   [clojure.spec.alpha           :as                s]
   [orchestra.spec.test          :as               st]
   [clojure.spec.test.alpha      :as              cst]
   [clojure.spec.gen.alpha       :as              gen]
   [clojure.test                 :refer   [run-tests]]
   [clojure.repl                 :refer          :all]
   [clojure.tools.namespace.find :as          ns-find]
   [clojure.tools.namespace.repl :refer [refresh
                                         refresh-all]]
   [expound.alpha                :as          expound]

   [puget.printer                :refer      [cprint]]
   [kaocha.repl                  :refer          :all]
   [infra])

  (:import [org.joda.money Money BigMoney CurrencyUnit MoneyUtils]
           [java.math RoundingMode]))

(def test-namespaces
  (->> "test/randomseed/money"
       clojure.java.io/file
       ns-find/find-namespaces-in-dir
       (filter #(clojure.string/ends-with? (str %) "-test"))))

(doseq [n test-namespaces]
  (require n))

(set! *warn-on-reflection* true)

(alter-var-root
 #'s/*explain-out*
 (constantly
  (expound/custom-printer {:show-valid-values? false
                           :print-specs?        true
                           :theme    :figwheel-theme})))

(when (System/getProperty "nrepl.load")
  (require 'nrepl))

(st/instrument)

(defn test-all []
  (refresh)
  (cst/with-instrument-disabled
    (apply run-tests test-namespaces)))

(comment 
  (refresh-all)
  (cst/with-instrument-disabled (test-all))
  (cst/with-instrument-disabled (run-all))
  )
