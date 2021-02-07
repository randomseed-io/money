(ns randomseed.money.rebel.main
  (:require
   rebel-readline.clojure.main
   rebel-readline.core
   io.aviso.ansi
   puget.printer))

(defn -main
  [& args]
  (rebel-readline.core/ensure-terminal
   (rebel-readline.clojure.main/repl
    :init (fn []
            (try
              (println "[money] Loading Clojure code, please wait...")
              (require 'user)
              (in-ns 'user)
              (catch Exception e
                (.printStackTrace e)
                (println "[money] Failed to require user, this usually means there was a syntax error. See exception above.")))))))
