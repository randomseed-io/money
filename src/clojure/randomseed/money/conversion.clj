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

(ns randomseed.money.conversion
  (:import java.math.RoundingMode))

;;
;; API
;;

(defprotocol ToRoundingMode
  (^java.math.RoundingMode to-rounding-mode [input] "Converts input to java.math.RoundingMode"))

(extend-protocol ToRoundingMode
  RoundingMode
  (to-rounding-mode [input]
    input)

  clojure.lang.Named
  (to-rounding-mode [^clojure.lang.Named input]
    (to-rounding-mode (name input)))

  String
  (to-rounding-mode [^String input]
    (case (.toLowerCase input)
      "up"   RoundingMode/UP
      "down" RoundingMode/DOWN
      "ceiling"   RoundingMode/CEILING
      "floor"     RoundingMode/FLOOR
      "half-up"   RoundingMode/HALF_UP
      "half-down" RoundingMode/HALF_DOWN
      "half-even" RoundingMode/HALF_EVEN
      "unnecessary" RoundingMode/UNNECESSARY
      RoundingMode/UNNECESSARY))

  nil
  (to-rounding-mode [_]
    RoundingMode/UNNECESSARY))
