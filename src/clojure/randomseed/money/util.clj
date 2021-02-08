;; This source code is dual-licensed under the Apache License, version
;; 2.0, and the Eclipse Public License, version 1.0.
;;
;; The APL v2.0:
;;
;; ----------------------------------------------------------------------------------
;; Copyright (c) 2021 Paweł Wilk and the random:seed team
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
;; All rights reserved.
;;
;; This program and the accompanying materials are made available under the terms of
;; the Eclipse Public License Version 1.0,
;; which accompanies this distribution and is available at
;; http://www.eclipse.org/legal/epl-v10.html.
;; ----------------------------------------------------------------------------------

(ns randomseed.money.util

  (:require [clojure.java.io           :as       io]
            [clojure.java.classpath    :as       cp]
            [clojure.data.csv          :as      csv]
            [clojure.edn               :as      edn])

  (:import [org.apache.commons.io.input BOMInputStream]
           [org.apache.commons.io        ByteOrderMark]))

(def ^String ^private ^const default-encoding
  "Default encoding for input files."
  "UTF-8")

(defn ^Boolean relative-path?
  "Returns true if the given path is relative. False otherwise."
  [^String pathname]
  (try (io/as-relative-path pathname)
       (catch IllegalArgumentException e
         false))
  true)

(def ^{:tag Boolean :arglists '([^String pathname])} absolute-path?
  "Returns true if the given path is absolute. False otherwise."
  (complement relative-path?))

(defn ^clojure.lang.LazySeq get-java-classpath-folders
  "Lists all directories which exist in Java classpath as a sequence of
  strings. Returns nil if there are none."
  []
  (seq (map str (filter #(.isDirectory (io/file %)) (cp/classpath)))))

(defn ^String resource-pathname
  "For the given pathnames creates a pathname expressed as a string that resides within
  one of the Java resource directories. The path must exist to be returned."
  ([]        (some-> (io/resource "") io/file str))
  ([& paths] (some->> paths (remove nil?) seq
                      (apply io/file) str
                      io/resource
                      io/file
                      str)))

(defn ^Boolean integer-string?
  [^String s]
  "Returns true if string contains valid integer number."
  (try
    (boolean (and (some? s) (Integer/parseInt s)))
    (catch NumberFormatException e false)))

(defn ^Integer try-parse-int
  [^String s]
  "Returns integer from a string or nil if the given string does not contain valid
  integer."
  (when (some? (seq s))
    (try (Integer/parseInt s)
         (catch NumberFormatException e nil))))

(def ^longs ^:private bom-utf-ary
  "Array of BOM encodings."
  (into-array [ByteOrderMark/UTF_16LE
               ByteOrderMark/UTF_16BE
               ByteOrderMark/UTF_8
               ByteOrderMark/UTF_32BE
               ByteOrderMark/UTF_32LE]))

(defn ^clojure.lang.LazySeq read-csv
  "Reads CSV file and returns a lazy sequence of rows."
  [^String filename]
  (let [stream   (io/input-stream filename)
        bstream  (BOMInputStream. stream true bom-utf-ary)
        bomenc   (.getBOM bstream)
        encoding (if (some? bomenc) (.getCharsetName bomenc) default-encoding)]
    (with-open [reader (io/reader bstream :encoding encoding)]
      (->> (doall (csv/read-csv reader))
           (remove (some-fn empty? (comp #{\#} ffirst)))))))

