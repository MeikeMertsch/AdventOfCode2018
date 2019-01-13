(ns christmas.chr18.day22-test
 (:require [christmas.chr18.day22 :as chr]
 		   [clojure.string :as str]
  		   [expectations :refer :all]))

(comment
(def ex-depth 510)
(def ex-columns 10)
(def ex-rows 10)
(def ex-target [10 10])


(def depth 7305)
(def columns 13)
(def rows 734)
(def target [13 734])


(expect 510 (chr/erosion-level ex-depth 0))
(expect 17317 (chr/erosion-level ex-depth 16807))
(expect 8415 (chr/erosion-level ex-depth 48271))
(expect 1805 (chr/erosion-level ex-depth 145722555))

(expect [510 0] (chr/pixel ex-depth 0))
(expect [17317 1] (chr/pixel ex-depth 16807))
(expect [8415 0] (chr/pixel ex-depth 48271))
(expect [1805 2] (chr/pixel ex-depth 145722555))

(expect [0 1 0 2 1 0 2 0 2 1 0] (map last (chr/row-0 ex-depth ex-columns)))
;(expect "" (chr/row-0 ex-depth ex-columns))

(expect [0 0 0 1 1 1 2 2 0 0 0] (map last (map (partial chr/px-0 ex-depth) (take 11 (range)))))

(expect [1805 2] (chr/next-px ex-target 3 ex-depth [17317 1] [8415 0]))

(expect [0 2 1 2 1 2 2 2 0 0 2] (map last (chr/next-row ex-target ex-depth (chr/row-0 ex-depth ex-columns) 1)))
;(expect [0 2 1 2 1 2 2 2 0 0 2] (chr/next-row ex-target ex-depth (chr/row-0 ex-depth ex-columns) 1))
(expect [0 2 1 2 1 2 0 0 1 2 1] (map last (chr/next-row [6 1] ex-depth (chr/row-0 ex-depth ex-columns) 1)))

;(expect "" (chr/all-pixels ex-depth ex-rows ex-columns))

(expect 114 (chr/day22a ex-depth ex-rows ex-columns))
(expect 10204 (chr/day22a depth rows columns))

(expect 0 (last (last (chr/terrain depth rows columns target))))
;(expect "" (chr/day22b depth (+ 8 rows) (+ 8 columns)))


(defn act-in-cave [cave func]
	(map func (map (partial map {0 "." 1 "=" 2 "|"}) cave)))

(defn act-on-board [area [columns  rows] func]
	(map (fn [row] 
			(func (map #({0 "." 1 "=" 2 "|"} (get area [% row] " ")) 
						(range columns))))
		 (range rows)))

(defn print-board [board size]
	(act-on-board board size #(println (apply str %))))
;(spit "log.txt" (apply str msg "\n") :append true)
(defn output-board [board size]
	(act-on-board board size #(spit "resources/day16-20/Output20b2" (str (apply str %) "\n") :append true)))


(defn print-cave [cave]
	(act-in-cave cave #(println (apply str %))))

(defn output-cave [cave]
	(act-in-cave cave #(spit "resources/day16-20/Output20b" (apply str (concat "\n" %)) :append true)))

;(expect "" (output-board (chr/transform-to-map ex-depth (+ 6 ex-rows) (+ 6 ex-columns) ex-target) [(+ 6 ex-columns) (+ 6 ex-rows)]))
;(expect "" (output-board (chr/transform-to-map depth (+ 8 rows) (+ 8 columns))))

(def around 7)
(def t-cave (chr/transform-to-map ex-depth (+ around ex-rows) (+ around ex-columns) ex-target))
(def cave (chr/transform-to-map depth (+ around rows) (+ around columns) target))
(def t-maxima [(+ around ex-columns) (+ around ex-rows)])
(def maxima [(+ around columns) (+ around rows)])

(def a {:tool 1 :dist 0})
(def b {:tool 1 :dist 1})
(def c {:tool 1 :dist 2})

(def t-state-0 {:perms {[0 0] 0} :temps {} :candidates {[0 0] 0} :tool [1] :cave t-cave :maxima t-maxima})

(def t-state-1 {:perms {[0 0] 0} :temps {[0 0] 0} :candidates {[0 1] 1} :tool [1] :cave t-cave :maxima t-maxima})
(def t-state-2 {:perms {[0 0] 0} :temps {[0 0] 0 [0 1] 1} :candidates {[1 1] 2 [0 2] 2} :tool [1] :cave t-cave :maxima t-maxima})
(def t-state-00 {:perms {[0 0] 0} :temps {[0 0] 0 [0 1] 1 [1 1] 2 [0 2] 2} :candidates {} :tool [1] :cave t-cave :maxima t-maxima})
(def t-state-01 [{:perms {[0 0] 0 [0 1] 1 [1 1] 2 [0 2] 2} :temps {} :candidates {[1 1] 9} :tool [0] :cave t-cave :maxima t-maxima} 
				 {:perms {[0 0] 0 [0 1] 1 [1 1] 2 [0 2] 2} :temps {} :candidates {[0 0] 7 [0 1] 8 [0 2] 9} :tool [2] :cave t-cave :maxima t-maxima}])

(def state-0 {:perms {[0 0] 0} :temps {} :candidates {[0 0] 0} :tool [1] :cave cave :maxima maxima})

(expect t-state-1  (chr/tick t-state-0))
(expect t-state-2  (chr/tick t-state-1))
;(expect-focused t-state-2 (chr/tick t-state-2))

(expect [0 2] (chr/next-tools t-state-00))
;(expect t-state-01 (map #(dissoc % :cave) (chr/change-tools t-state-00)))
;(expect t-state-01 (chr/all-tool-changes t-state-00))

;(expect {:perms {[0 0] 0, [0 1] 1, [1 1] 2, [0 2] 2}, :temps {}, :candidates {[1 1] 9}, :tool 0, :maxima [18 18]}  (dissoc (chr/switch-tool (chr/all-tool-changes t-state-00) 0) :cave))
;(expect {:perms {[0 0] 0, [0 1] 1, [1 1] 2, [0 2] 2}, :temps {}, :candidates {[0 0] 7, [0 1] 8, [0 2] 9}, :tool 2, :maxima [18 18]}  (dissoc (chr/switch-tool (chr/all-tool-changes t-state-00) 2) :cave))
(def four-things [{:perms {[7 1] 15, [2 2] 11, [0 0] 0, [1 0] 10, [2 3] 12, [1 1] 2, [3 0] 12, [4 1] 12, [5 1] 13, [6 1] 14, [0 2] 2, [3 1] 11, [2 1] 10, [6 0] 15, [1 2] 10, [3 2] 12, [0 1] 1, [4 0] 13}, :temps {}, :candidates {[7 1] 22, [2 3] 19, [1 1] 16, [3 0] 19, [5 1] 20, [6 1] 21, [3 1] 18, [6 0] 22, [3 2] 19}, :tool 1, :cave {[8 8] 0, [7 6] 2, [8 7] 0, [16 6] 2, [3 15] 2, [8 11] 1, [9 8] 1, [7 1] 2, [10 14] 1, [12 12] 1, [8 9] 2, [7 12] 0, [2 18] 0, [8 18] 2, [12 18] 0, [12 6] 1, [15 4] 0, [13 3] 1, [10 5] 2, [13 15] 0, [15 11] 2, [11 9] 2, [11 2] 0, [4 3] 0, [7 11] 2, [17 5] 1, [2 2] 1, [12 17] 0, [7 13] 0, [18 12] 2, [0 0] 0, [9 18] 0, [3 9] 1, [13 8] 2, [4 12] 1, [7 7] 1, [18 3] 2, [2 8] 0, [10 15] 2, [13 6] 0, [14 17] 1, [17 2] 2, [1 0] 1, [8 4] 0, [17 6] 2, [2 3] 2, [18 7] 0, [2 5] 2, [7 2] 0, [16 5] 2, [15 16] 0, [10 13] 1, [15 17] 2, [18 0] 0, [6 7] 2, [15 0] 0, [12 13] 0, [15 12] 1, [12 14] 2, [7 4] 0, [8 3] 0, [9 15] 2, [0 6] 2, [3 3] 0, [13 12] 2, [10 9] 1, [5 4] 1, [3 16] 2, [15 3] 0, [10 8] 2, [5 10] 1, [18 2] 2, [1 1] 2, [6 3] 0, [7 18] 1, [17 16] 2, [0 5] 1, [14 6] 0, [3 4] 0, [17 18] 1, [11 14] 2, [11 0] 2, [17 0] 1, [12 8] 2, [1 12] 0, [12 5] 0, [7 3] 2, [8 6] 2, [12 2] 0, [16 7] 1, [17 12] 1, [1 15] 2, [15 9] 1, [17 1] 1, [4 2] 0, [14 13] 1, [0 18] 1, [7 8] 0, [9 16] 2, [3 12] 2, [4 14] 2, [13 2] 2, [18 10] 1, [3 0] 2, [10 17] 1, [18 18] 0, [9 0] 1, [6 6] 1, [9 6] 2, [1 13] 2, [12 1] 1, [11 13] 0, [13 9] 0, [6 13] 0, [16 2] 0, [1 9] 1, [18 11] 2, [0 13] 2, [11 18] 0, [8 10] 1, [5 3] 0, [18 4] 2, [18 14] 1, [9 9] 2, [13 7] 1, [9 3] 1, [0 17] 0, [9 12] 1, [4 7] 1, [13 1] 0, [4 10] 2, [4 15] 1, [0 15] 2, [0 14] 2, [3 13] 2, [4 9] 1, [1 10] 1, [2 9] 1, [18 9] 1, [6 5] 1, [11 11] 0, [5 13] 0, [13 0] 2, [3 17] 2, [5 14] 2, [16 13] 1, [4 11] 0, [18 6] 2, [5 16] 0, [0 9] 0, [8 0] 2, [4 1] 1, [5 2] 0, [2 13] 1, [16 3] 0, [4 6] 1, [14 15] 2, [15 7] 1, [17 4] 1, [16 8] 2, [11 4] 2, [1 4] 2, [10 2] 1, [11 8] 0, [1 11] 2, [5 7] 2, [11 12] 1, [10 16] 1, [16 16] 2, [12 7] 2, [12 0] 1, [8 2] 2, [4 18] 2, [16 0] 2, [10 7] 1, [10 0] 0, [11 10] 2, [1 3] 0, [4 8] 1, [10 11] 2, [18 1] 1, [12 11] 2, [15 1] 0, [1 5] 2, [9 14] 0, [13 13] 2, [12 10] 2, [11 6] 0, [14 4] 0, [11 3] 0, [15 10] 1, [1 8] 1, [8 16] 2, [1 7] 0, [12 4] 1, [16 17] 2, [15 18] 2, [6 4] 0, [15 5] 1, [18 8] 0, [18 17] 0, [1 18] 0, [8 1] 0, [2 12] 1, [16 11] 2, [14 8] 2, [18 16] 1, [0 3] 1, [5 1] 2, [6 1] 2, [2 11] 2, [6 14] 1, [5 11] 0, [1 16] 2, [5 6] 1, [5 8] 1, [16 18] 0, [2 15] 0, [13 11] 2, [8 15] 0, [6 18] 0, [8 13] 2, [8 5] 2, [0 7] 2, [4 17] 0, [3 18] 0, [9 17] 0, [14 11] 2, [15 8] 0, [6 8] 1, [13 5] 0, [9 11] 1, [6 11] 0, [10 1] 2, [5 5] 0, [7 9] 2, [10 12] 2, [14 1] 0, [2 7] 0, [11 1] 0, [5 17] 2, [2 17] 2, [13 17] 1, [15 6] 2, [5 9] 1, [2 4] 0, [3 6] 0, [5 15] 1, [6 15] 0, [12 15] 1, [14 5] 2, [7 10] 1, [17 3] 1, [10 6] 0, [17 14] 0, [17 10] 0, [9 2] 2, [0 12] 1, [4 5] 1, [11 7] 1, [9 1] 0, [9 7] 2, [10 4] 0, [17 15] 2, [15 13] 0, [10 10] 2, [7 0] 0, [0 16] 0, [12 9] 1, [0 2] 0, [6 9] 1, [17 11] 2, [11 15] 1, [4 16] 0, [3 14] 1, [16 12] 2, [2 0] 0, [0 4] 1, [3 11] 2, [10 18] 1, [14 18] 2, [0 10] 0, [11 5] 1, [9 13] 1, [3 1] 2, [3 10] 1, [12 3] 2, [16 14] 1, [13 16] 2, [17 13] 0, [14 14] 1, [1 17] 1, [2 1] 1, [6 17] 1, [1 14] 1, [9 5] 1, [13 18] 1, [6 10] 2, [3 8] 0, [9 4] 1, [18 15] 1, [7 14] 1, [14 2] 1, [0 11] 1, [6 12] 1, [16 1] 0, [14 16] 2, [16 15] 0, [4 13] 1, [1 6] 0, [2 14] 0, [16 9] 2, [14 3] 1, [4 4] 1, [11 17] 1, [7 16] 2, [3 7] 1, [2 10] 1, [7 5] 2, [7 15] 2, [13 10] 1, [2 6] 1, [16 10] 0, [5 0] 0, [6 2] 0, [14 12] 1, [17 9] 2, [9 10] 1, [8 14] 1, [8 12] 0, [13 4] 1, [6 0] 2, [7 17] 2, [16 4] 1, [15 15] 2, [12 16] 2, [14 7] 1, [1 2] 1, [14 10] 2, [6 16] 2, [5 18] 2, [15 14] 1, [2 16] 0, [10 3] 1, [18 13] 0, [3 5] 0, [13 14] 2, [0 8] 0, [5 12] 0, [3 2] 2, [17 8] 1, [14 9] 2, [18 5] 2, [11 16] 0, [17 17] 1, [14 0] 1, [0 1] 0, [15 2] 1, [8 17] 0, [4 0] 1, [17 7] 2}, :maxima [18 18]} {:perms {[7 1] 15, [2 2] 11, [0 0] 0, [1 0] 10, [2 3] 12, [1 1] 2, [3 0] 12, [4 1] 12, [5 1] 13, [6 1] 14, [0 2] 2, [3 1] 11, [2 1] 10, [6 0] 15, [1 2] 10, [3 2] 12, [0 1] 1, [4 0] 13}, :temps {}, :candidates {[2 2] 18, [1 0] 17, [4 1] 19, [2 1] 17, [1 2] 17, [4 0] 20}, :tool 2, :cave {[8 8] 0, [7 6] 2, [8 7] 0, [16 6] 2, [3 15] 2, [8 11] 1, [9 8] 1, [7 1] 2, [10 14] 1, [12 12] 1, [8 9] 2, [7 12] 0, [2 18] 0, [8 18] 2, [12 18] 0, [12 6] 1, [15 4] 0, [13 3] 1, [10 5] 2, [13 15] 0, [15 11] 2, [11 9] 2, [11 2] 0, [4 3] 0, [7 11] 2, [17 5] 1, [2 2] 1, [12 17] 0, [7 13] 0, [18 12] 2, [0 0] 0, [9 18] 0, [3 9] 1, [13 8] 2, [4 12] 1, [7 7] 1, [18 3] 2, [2 8] 0, [10 15] 2, [13 6] 0, [14 17] 1, [17 2] 2, [1 0] 1, [8 4] 0, [17 6] 2, [2 3] 2, [18 7] 0, [2 5] 2, [7 2] 0, [16 5] 2, [15 16] 0, [10 13] 1, [15 17] 2, [18 0] 0, [6 7] 2, [15 0] 0, [12 13] 0, [15 12] 1, [12 14] 2, [7 4] 0, [8 3] 0, [9 15] 2, [0 6] 2, [3 3] 0, [13 12] 2, [10 9] 1, [5 4] 1, [3 16] 2, [15 3] 0, [10 8] 2, [5 10] 1, [18 2] 2, [1 1] 2, [6 3] 0, [7 18] 1, [17 16] 2, [0 5] 1, [14 6] 0, [3 4] 0, [17 18] 1, [11 14] 2, [11 0] 2, [17 0] 1, [12 8] 2, [1 12] 0, [12 5] 0, [7 3] 2, [8 6] 2, [12 2] 0, [16 7] 1, [17 12] 1, [1 15] 2, [15 9] 1, [17 1] 1, [4 2] 0, [14 13] 1, [0 18] 1, [7 8] 0, [9 16] 2, [3 12] 2, [4 14] 2, [13 2] 2, [18 10] 1, [3 0] 2, [10 17] 1, [18 18] 0, [9 0] 1, [6 6] 1, [9 6] 2, [1 13] 2, [12 1] 1, [11 13] 0, [13 9] 0, [6 13] 0, [16 2] 0, [1 9] 1, [18 11] 2, [0 13] 2, [11 18] 0, [8 10] 1, [5 3] 0, [18 4] 2, [18 14] 1, [9 9] 2, [13 7] 1, [9 3] 1, [0 17] 0, [9 12] 1, [4 7] 1, [13 1] 0, [4 10] 2, [4 15] 1, [0 15] 2, [0 14] 2, [3 13] 2, [4 9] 1, [1 10] 1, [2 9] 1, [18 9] 1, [6 5] 1, [11 11] 0, [5 13] 0, [13 0] 2, [3 17] 2, [5 14] 2, [16 13] 1, [4 11] 0, [18 6] 2, [5 16] 0, [0 9] 0, [8 0] 2, [4 1] 1, [5 2] 0, [2 13] 1, [16 3] 0, [4 6] 1, [14 15] 2, [15 7] 1, [17 4] 1, [16 8] 2, [11 4] 2, [1 4] 2, [10 2] 1, [11 8] 0, [1 11] 2, [5 7] 2, [11 12] 1, [10 16] 1, [16 16] 2, [12 7] 2, [12 0] 1, [8 2] 2, [4 18] 2, [16 0] 2, [10 7] 1, [10 0] 0, [11 10] 2, [1 3] 0, [4 8] 1, [10 11] 2, [18 1] 1, [12 11] 2, [15 1] 0, [1 5] 2, [9 14] 0, [13 13] 2, [12 10] 2, [11 6] 0, [14 4] 0, [11 3] 0, [15 10] 1, [1 8] 1, [8 16] 2, [1 7] 0, [12 4] 1, [16 17] 2, [15 18] 2, [6 4] 0, [15 5] 1, [18 8] 0, [18 17] 0, [1 18] 0, [8 1] 0, [2 12] 1, [16 11] 2, [14 8] 2, [18 16] 1, [0 3] 1, [5 1] 2, [6 1] 2, [2 11] 2, [6 14] 1, [5 11] 0, [1 16] 2, [5 6] 1, [5 8] 1, [16 18] 0, [2 15] 0, [13 11] 2, [8 15] 0, [6 18] 0, [8 13] 2, [8 5] 2, [0 7] 2, [4 17] 0, [3 18] 0, [9 17] 0, [14 11] 2, [15 8] 0, [6 8] 1, [13 5] 0, [9 11] 1, [6 11] 0, [10 1] 2, [5 5] 0, [7 9] 2, [10 12] 2, [14 1] 0, [2 7] 0, [11 1] 0, [5 17] 2, [2 17] 2, [13 17] 1, [15 6] 2, [5 9] 1, [2 4] 0, [3 6] 0, [5 15] 1, [6 15] 0, [12 15] 1, [14 5] 2, [7 10] 1, [17 3] 1, [10 6] 0, [17 14] 0, [17 10] 0, [9 2] 2, [0 12] 1, [4 5] 1, [11 7] 1, [9 1] 0, [9 7] 2, [10 4] 0, [17 15] 2, [15 13] 0, [10 10] 2, [7 0] 0, [0 16] 0, [12 9] 1, [0 2] 0, [6 9] 1, [17 11] 2, [11 15] 1, [4 16] 0, [3 14] 1, [16 12] 2, [2 0] 0, [0 4] 1, [3 11] 2, [10 18] 1, [14 18] 2, [0 10] 0, [11 5] 1, [9 13] 1, [3 1] 2, [3 10] 1, [12 3] 2, [16 14] 1, [13 16] 2, [17 13] 0, [14 14] 1, [1 17] 1, [2 1] 1, [6 17] 1, [1 14] 1, [9 5] 1, [13 18] 1, [6 10] 2, [3 8] 0, [9 4] 1, [18 15] 1, [7 14] 1, [14 2] 1, [0 11] 1, [6 12] 1, [16 1] 0, [14 16] 2, [16 15] 0, [4 13] 1, [1 6] 0, [2 14] 0, [16 9] 2, [14 3] 1, [4 4] 1, [11 17] 1, [7 16] 2, [3 7] 1, [2 10] 1, [7 5] 2, [7 15] 2, [13 10] 1, [2 6] 1, [16 10] 0, [5 0] 0, [6 2] 0, [14 12] 1, [17 9] 2, [9 10] 1, [8 14] 1, [8 12] 0, [13 4] 1, [6 0] 2, [7 17] 2, [16 4] 1, [15 15] 2, [12 16] 2, [14 7] 1, [1 2] 1, [14 10] 2, [6 16] 2, [5 18] 2, [15 14] 1, [2 16] 0, [10 3] 1, [18 13] 0, [3 5] 0, [13 14] 2, [0 8] 0, [5 12] 0, [3 2] 2, [17 8] 1, [14 9] 2, [18 5] 2, [11 16] 0, [17 17] 1, [14 0] 1, [0 1] 0, [15 2] 1, [8 17] 0, [4 0] 1, [17 7] 2}, :maxima [18 18]} {:perms {[2 2] 11, [0 0] 0, [1 0] 8, [1 1] 2, [0 5] 12, [1 3] 11, [0 3] 10, [0 2] 2, [2 0] 9, [0 4] 11, [2 1] 10, [1 2] 10, [0 1] 1}, :temps {}, :candidates {[2 2] 18, [1 0] 15, [0 5] 19, [0 3] 17, [0 4] 18, [2 1] 17, [1 2] 17}, :tool 0, :cave {[8 8] 0, [7 6] 2, [8 7] 0, [16 6] 2, [3 15] 2, [8 11] 1, [9 8] 1, [7 1] 2, [10 14] 1, [12 12] 1, [8 9] 2, [7 12] 0, [2 18] 0, [8 18] 2, [12 18] 0, [12 6] 1, [15 4] 0, [13 3] 1, [10 5] 2, [13 15] 0, [15 11] 2, [11 9] 2, [11 2] 0, [4 3] 0, [7 11] 2, [17 5] 1, [2 2] 1, [12 17] 0, [7 13] 0, [18 12] 2, [0 0] 0, [9 18] 0, [3 9] 1, [13 8] 2, [4 12] 1, [7 7] 1, [18 3] 2, [2 8] 0, [10 15] 2, [13 6] 0, [14 17] 1, [17 2] 2, [1 0] 1, [8 4] 0, [17 6] 2, [2 3] 2, [18 7] 0, [2 5] 2, [7 2] 0, [16 5] 2, [15 16] 0, [10 13] 1, [15 17] 2, [18 0] 0, [6 7] 2, [15 0] 0, [12 13] 0, [15 12] 1, [12 14] 2, [7 4] 0, [8 3] 0, [9 15] 2, [0 6] 2, [3 3] 0, [13 12] 2, [10 9] 1, [5 4] 1, [3 16] 2, [15 3] 0, [10 8] 2, [5 10] 1, [18 2] 2, [1 1] 2, [6 3] 0, [7 18] 1, [17 16] 2, [0 5] 1, [14 6] 0, [3 4] 0, [17 18] 1, [11 14] 2, [11 0] 2, [17 0] 1, [12 8] 2, [1 12] 0, [12 5] 0, [7 3] 2, [8 6] 2, [12 2] 0, [16 7] 1, [17 12] 1, [1 15] 2, [15 9] 1, [17 1] 1, [4 2] 0, [14 13] 1, [0 18] 1, [7 8] 0, [9 16] 2, [3 12] 2, [4 14] 2, [13 2] 2, [18 10] 1, [3 0] 2, [10 17] 1, [18 18] 0, [9 0] 1, [6 6] 1, [9 6] 2, [1 13] 2, [12 1] 1, [11 13] 0, [13 9] 0, [6 13] 0, [16 2] 0, [1 9] 1, [18 11] 2, [0 13] 2, [11 18] 0, [8 10] 1, [5 3] 0, [18 4] 2, [18 14] 1, [9 9] 2, [13 7] 1, [9 3] 1, [0 17] 0, [9 12] 1, [4 7] 1, [13 1] 0, [4 10] 2, [4 15] 1, [0 15] 2, [0 14] 2, [3 13] 2, [4 9] 1, [1 10] 1, [2 9] 1, [18 9] 1, [6 5] 1, [11 11] 0, [5 13] 0, [13 0] 2, [3 17] 2, [5 14] 2, [16 13] 1, [4 11] 0, [18 6] 2, [5 16] 0, [0 9] 0, [8 0] 2, [4 1] 1, [5 2] 0, [2 13] 1, [16 3] 0, [4 6] 1, [14 15] 2, [15 7] 1, [17 4] 1, [16 8] 2, [11 4] 2, [1 4] 2, [10 2] 1, [11 8] 0, [1 11] 2, [5 7] 2, [11 12] 1, [10 16] 1, [16 16] 2, [12 7] 2, [12 0] 1, [8 2] 2, [4 18] 2, [16 0] 2, [10 7] 1, [10 0] 0, [11 10] 2, [1 3] 0, [4 8] 1, [10 11] 2, [18 1] 1, [12 11] 2, [15 1] 0, [1 5] 2, [9 14] 0, [13 13] 2, [12 10] 2, [11 6] 0, [14 4] 0, [11 3] 0, [15 10] 1, [1 8] 1, [8 16] 2, [1 7] 0, [12 4] 1, [16 17] 2, [15 18] 2, [6 4] 0, [15 5] 1, [18 8] 0, [18 17] 0, [1 18] 0, [8 1] 0, [2 12] 1, [16 11] 2, [14 8] 2, [18 16] 1, [0 3] 1, [5 1] 2, [6 1] 2, [2 11] 2, [6 14] 1, [5 11] 0, [1 16] 2, [5 6] 1, [5 8] 1, [16 18] 0, [2 15] 0, [13 11] 2, [8 15] 0, [6 18] 0, [8 13] 2, [8 5] 2, [0 7] 2, [4 17] 0, [3 18] 0, [9 17] 0, [14 11] 2, [15 8] 0, [6 8] 1, [13 5] 0, [9 11] 1, [6 11] 0, [10 1] 2, [5 5] 0, [7 9] 2, [10 12] 2, [14 1] 0, [2 7] 0, [11 1] 0, [5 17] 2, [2 17] 2, [13 17] 1, [15 6] 2, [5 9] 1, [2 4] 0, [3 6] 0, [5 15] 1, [6 15] 0, [12 15] 1, [14 5] 2, [7 10] 1, [17 3] 1, [10 6] 0, [17 14] 0, [17 10] 0, [9 2] 2, [0 12] 1, [4 5] 1, [11 7] 1, [9 1] 0, [9 7] 2, [10 4] 0, [17 15] 2, [15 13] 0, [10 10] 2, [7 0] 0, [0 16] 0, [12 9] 1, [0 2] 0, [6 9] 1, [17 11] 2, [11 15] 1, [4 16] 0, [3 14] 1, [16 12] 2, [2 0] 0, [0 4] 1, [3 11] 2, [10 18] 1, [14 18] 2, [0 10] 0, [11 5] 1, [9 13] 1, [3 1] 2, [3 10] 1, [12 3] 2, [16 14] 1, [13 16] 2, [17 13] 0, [14 14] 1, [1 17] 1, [2 1] 1, [6 17] 1, [1 14] 1, [9 5] 1, [13 18] 1, [6 10] 2, [3 8] 0, [9 4] 1, [18 15] 1, [7 14] 1, [14 2] 1, [0 11] 1, [6 12] 1, [16 1] 0, [14 16] 2, [16 15] 0, [4 13] 1, [1 6] 0, [2 14] 0, [16 9] 2, [14 3] 1, [4 4] 1, [11 17] 1, [7 16] 2, [3 7] 1, [2 10] 1, [7 5] 2, [7 15] 2, [13 10] 1, [2 6] 1, [16 10] 0, [5 0] 0, [6 2] 0, [14 12] 1, [17 9] 2, [9 10] 1, [8 14] 1, [8 12] 0, [13 4] 1, [6 0] 2, [7 17] 2, [16 4] 1, [15 15] 2, [12 16] 2, [14 7] 1, [1 2] 1, [14 10] 2, [6 16] 2, [5 18] 2, [15 14] 1, [2 16] 0, [10 3] 1, [18 13] 0, [3 5] 0, [13 14] 2, [0 8] 0, [5 12] 0, [3 2] 2, [17 8] 1, [14 9] 2, [18 5] 2, [11 16] 0, [17 17] 1, [14 0] 1, [0 1] 0, [15 2] 1, [8 17] 0, [4 0] 1, [17 7] 2}, :maxima [18 18]} {:perms {[2 2] 11, [0 0] 0, [1 0] 8, [1 1] 2, [0 5] 12, [1 3] 11, [0 3] 10, [0 2] 2, [2 0] 9, [0 4] 11, [2 1] 10, [1 2] 10, [0 1] 1}, :temps {}, :candidates {[0 0] 14, [1 3] 18, [0 2] 16, [2 0] 16, [0 1] 15}, :tool 1, :cave {[8 8] 0, [7 6] 2, [8 7] 0, [16 6] 2, [3 15] 2, [8 11] 1, [9 8] 1, [7 1] 2, [10 14] 1, [12 12] 1, [8 9] 2, [7 12] 0, [2 18] 0, [8 18] 2, [12 18] 0, [12 6] 1, [15 4] 0, [13 3] 1, [10 5] 2, [13 15] 0, [15 11] 2, [11 9] 2, [11 2] 0, [4 3] 0, [7 11] 2, [17 5] 1, [2 2] 1, [12 17] 0, [7 13] 0, [18 12] 2, [0 0] 0, [9 18] 0, [3 9] 1, [13 8] 2, [4 12] 1, [7 7] 1, [18 3] 2, [2 8] 0, [10 15] 2, [13 6] 0, [14 17] 1, [17 2] 2, [1 0] 1, [8 4] 0, [17 6] 2, [2 3] 2, [18 7] 0, [2 5] 2, [7 2] 0, [16 5] 2, [15 16] 0, [10 13] 1, [15 17] 2, [18 0] 0, [6 7] 2, [15 0] 0, [12 13] 0, [15 12] 1, [12 14] 2, [7 4] 0, [8 3] 0, [9 15] 2, [0 6] 2, [3 3] 0, [13 12] 2, [10 9] 1, [5 4] 1, [3 16] 2, [15 3] 0, [10 8] 2, [5 10] 1, [18 2] 2, [1 1] 2, [6 3] 0, [7 18] 1, [17 16] 2, [0 5] 1, [14 6] 0, [3 4] 0, [17 18] 1, [11 14] 2, [11 0] 2, [17 0] 1, [12 8] 2, [1 12] 0, [12 5] 0, [7 3] 2, [8 6] 2, [12 2] 0, [16 7] 1, [17 12] 1, [1 15] 2, [15 9] 1, [17 1] 1, [4 2] 0, [14 13] 1, [0 18] 1, [7 8] 0, [9 16] 2, [3 12] 2, [4 14] 2, [13 2] 2, [18 10] 1, [3 0] 2, [10 17] 1, [18 18] 0, [9 0] 1, [6 6] 1, [9 6] 2, [1 13] 2, [12 1] 1, [11 13] 0, [13 9] 0, [6 13] 0, [16 2] 0, [1 9] 1, [18 11] 2, [0 13] 2, [11 18] 0, [8 10] 1, [5 3] 0, [18 4] 2, [18 14] 1, [9 9] 2, [13 7] 1, [9 3] 1, [0 17] 0, [9 12] 1, [4 7] 1, [13 1] 0, [4 10] 2, [4 15] 1, [0 15] 2, [0 14] 2, [3 13] 2, [4 9] 1, [1 10] 1, [2 9] 1, [18 9] 1, [6 5] 1, [11 11] 0, [5 13] 0, [13 0] 2, [3 17] 2, [5 14] 2, [16 13] 1, [4 11] 0, [18 6] 2, [5 16] 0, [0 9] 0, [8 0] 2, [4 1] 1, [5 2] 0, [2 13] 1, [16 3] 0, [4 6] 1, [14 15] 2, [15 7] 1, [17 4] 1, [16 8] 2, [11 4] 2, [1 4] 2, [10 2] 1, [11 8] 0, [1 11] 2, [5 7] 2, [11 12] 1, [10 16] 1, [16 16] 2, [12 7] 2, [12 0] 1, [8 2] 2, [4 18] 2, [16 0] 2, [10 7] 1, [10 0] 0, [11 10] 2, [1 3] 0, [4 8] 1, [10 11] 2, [18 1] 1, [12 11] 2, [15 1] 0, [1 5] 2, [9 14] 0, [13 13] 2, [12 10] 2, [11 6] 0, [14 4] 0, [11 3] 0, [15 10] 1, [1 8] 1, [8 16] 2, [1 7] 0, [12 4] 1, [16 17] 2, [15 18] 2, [6 4] 0, [15 5] 1, [18 8] 0, [18 17] 0, [1 18] 0, [8 1] 0, [2 12] 1, [16 11] 2, [14 8] 2, [18 16] 1, [0 3] 1, [5 1] 2, [6 1] 2, [2 11] 2, [6 14] 1, [5 11] 0, [1 16] 2, [5 6] 1, [5 8] 1, [16 18] 0, [2 15] 0, [13 11] 2, [8 15] 0, [6 18] 0, [8 13] 2, [8 5] 2, [0 7] 2, [4 17] 0, [3 18] 0, [9 17] 0, [14 11] 2, [15 8] 0, [6 8] 1, [13 5] 0, [9 11] 1, [6 11] 0, [10 1] 2, [5 5] 0, [7 9] 2, [10 12] 2, [14 1] 0, [2 7] 0, [11 1] 0, [5 17] 2, [2 17] 2, [13 17] 1, [15 6] 2, [5 9] 1, [2 4] 0, [3 6] 0, [5 15] 1, [6 15] 0, [12 15] 1, [14 5] 2, [7 10] 1, [17 3] 1, [10 6] 0, [17 14] 0, [17 10] 0, [9 2] 2, [0 12] 1, [4 5] 1, [11 7] 1, [9 1] 0, [9 7] 2, [10 4] 0, [17 15] 2, [15 13] 0, [10 10] 2, [7 0] 0, [0 16] 0, [12 9] 1, [0 2] 0, [6 9] 1, [17 11] 2, [11 15] 1, [4 16] 0, [3 14] 1, [16 12] 2, [2 0] 0, [0 4] 1, [3 11] 2, [10 18] 1, [14 18] 2, [0 10] 0, [11 5] 1, [9 13] 1, [3 1] 2, [3 10] 1, [12 3] 2, [16 14] 1, [13 16] 2, [17 13] 0, [14 14] 1, [1 17] 1, [2 1] 1, [6 17] 1, [1 14] 1, [9 5] 1, [13 18] 1, [6 10] 2, [3 8] 0, [9 4] 1, [18 15] 1, [7 14] 1, [14 2] 1, [0 11] 1, [6 12] 1, [16 1] 0, [14 16] 2, [16 15] 0, [4 13] 1, [1 6] 0, [2 14] 0, [16 9] 2, [14 3] 1, [4 4] 1, [11 17] 1, [7 16] 2, [3 7] 1, [2 10] 1, [7 5] 2, [7 15] 2, [13 10] 1, [2 6] 1, [16 10] 0, [5 0] 0, [6 2] 0, [14 12] 1, [17 9] 2, [9 10] 1, [8 14] 1, [8 12] 0, [13 4] 1, [6 0] 2, [7 17] 2, [16 4] 1, [15 15] 2, [12 16] 2, [14 7] 1, [1 2] 1, [14 10] 2, [6 16] 2, [5 18] 2, [15 14] 1, [2 16] 0, [10 3] 1, [18 13] 0, [3 5] 0, [13 14] 2, [0 8] 0, [5 12] 0, [3 2] 2, [17 8] 1, [14 9] 2, [18 5] 2, [11 16] 0, [17 17] 1, [14 0] 1, [0 1] 0, [15 2] 1, [8 17] 0, [4 0] 1, [17 7] 2}, :maxima [18 18]}])
(def two-things [{:perms {[0 0] 0, [0 1] 1, [1 1] 2, [0 2] 2}, :temps {}, :candidates {[1 1] 9}, :tool 0, :cave {[8 8] 0, [7 6] 2, [8 7] 0, [16 6] 2, [3 15] 2, [8 11] 1, [9 8] 1, [7 1] 2, [10 14] 1, [12 12] 1, [8 9] 2, [7 12] 0, [2 18] 0, [8 18] 2, [12 18] 0, [12 6] 1, [15 4] 0, [13 3] 1, [10 5] 2, [13 15] 0, [15 11] 2, [11 9] 2, [11 2] 0, [4 3] 0, [7 11] 2, [17 5] 1, [2 2] 1, [12 17] 0, [7 13] 0, [18 12] 2, [0 0] 0, [9 18] 0, [3 9] 1, [13 8] 2, [4 12] 1, [7 7] 1, [18 3] 2, [2 8] 0, [10 15] 2, [13 6] 0, [14 17] 1, [17 2] 2, [1 0] 1, [8 4] 0, [17 6] 2, [2 3] 2, [18 7] 0, [2 5] 2, [7 2] 0, [16 5] 2, [15 16] 0, [10 13] 1, [15 17] 2, [18 0] 0, [6 7] 2, [15 0] 0, [12 13] 0, [15 12] 1, [12 14] 2, [7 4] 0, [8 3] 0, [9 15] 2, [0 6] 2, [3 3] 0, [13 12] 2, [10 9] 1, [5 4] 1, [3 16] 2, [15 3] 0, [10 8] 2, [5 10] 1, [18 2] 2, [1 1] 2, [6 3] 0, [7 18] 1, [17 16] 2, [0 5] 1, [14 6] 0, [3 4] 0, [17 18] 1, [11 14] 2, [11 0] 2, [17 0] 1, [12 8] 2, [1 12] 0, [12 5] 0, [7 3] 2, [8 6] 2, [12 2] 0, [16 7] 1, [17 12] 1, [1 15] 2, [15 9] 1, [17 1] 1, [4 2] 0, [14 13] 1, [0 18] 1, [7 8] 0, [9 16] 2, [3 12] 2, [4 14] 2, [13 2] 2, [18 10] 1, [3 0] 2, [10 17] 1, [18 18] 0, [9 0] 1, [6 6] 1, [9 6] 2, [1 13] 2, [12 1] 1, [11 13] 0, [13 9] 0, [6 13] 0, [16 2] 0, [1 9] 1, [18 11] 2, [0 13] 2, [11 18] 0, [8 10] 1, [5 3] 0, [18 4] 2, [18 14] 1, [9 9] 2, [13 7] 1, [9 3] 1, [0 17] 0, [9 12] 1, [4 7] 1, [13 1] 0, [4 10] 2, [4 15] 1, [0 15] 2, [0 14] 2, [3 13] 2, [4 9] 1, [1 10] 1, [2 9] 1, [18 9] 1, [6 5] 1, [11 11] 0, [5 13] 0, [13 0] 2, [3 17] 2, [5 14] 2, [16 13] 1, [4 11] 0, [18 6] 2, [5 16] 0, [0 9] 0, [8 0] 2, [4 1] 1, [5 2] 0, [2 13] 1, [16 3] 0, [4 6] 1, [14 15] 2, [15 7] 1, [17 4] 1, [16 8] 2, [11 4] 2, [1 4] 2, [10 2] 1, [11 8] 0, [1 11] 2, [5 7] 2, [11 12] 1, [10 16] 1, [16 16] 2, [12 7] 2, [12 0] 1, [8 2] 2, [4 18] 2, [16 0] 2, [10 7] 1, [10 0] 0, [11 10] 2, [1 3] 0, [4 8] 1, [10 11] 2, [18 1] 1, [12 11] 2, [15 1] 0, [1 5] 2, [9 14] 0, [13 13] 2, [12 10] 2, [11 6] 0, [14 4] 0, [11 3] 0, [15 10] 1, [1 8] 1, [8 16] 2, [1 7] 0, [12 4] 1, [16 17] 2, [15 18] 2, [6 4] 0, [15 5] 1, [18 8] 0, [18 17] 0, [1 18] 0, [8 1] 0, [2 12] 1, [16 11] 2, [14 8] 2, [18 16] 1, [0 3] 1, [5 1] 2, [6 1] 2, [2 11] 2, [6 14] 1, [5 11] 0, [1 16] 2, [5 6] 1, [5 8] 1, [16 18] 0, [2 15] 0, [13 11] 2, [8 15] 0, [6 18] 0, [8 13] 2, [8 5] 2, [0 7] 2, [4 17] 0, [3 18] 0, [9 17] 0, [14 11] 2, [15 8] 0, [6 8] 1, [13 5] 0, [9 11] 1, [6 11] 0, [10 1] 2, [5 5] 0, [7 9] 2, [10 12] 2, [14 1] 0, [2 7] 0, [11 1] 0, [5 17] 2, [2 17] 2, [13 17] 1, [15 6] 2, [5 9] 1, [2 4] 0, [3 6] 0, [5 15] 1, [6 15] 0, [12 15] 1, [14 5] 2, [7 10] 1, [17 3] 1, [10 6] 0, [17 14] 0, [17 10] 0, [9 2] 2, [0 12] 1, [4 5] 1, [11 7] 1, [9 1] 0, [9 7] 2, [10 4] 0, [17 15] 2, [15 13] 0, [10 10] 2, [7 0] 0, [0 16] 0, [12 9] 1, [0 2] 0, [6 9] 1, [17 11] 2, [11 15] 1, [4 16] 0, [3 14] 1, [16 12] 2, [2 0] 0, [0 4] 1, [3 11] 2, [10 18] 1, [14 18] 2, [0 10] 0, [11 5] 1, [9 13] 1, [3 1] 2, [3 10] 1, [12 3] 2, [16 14] 1, [13 16] 2, [17 13] 0, [14 14] 1, [1 17] 1, [2 1] 1, [6 17] 1, [1 14] 1, [9 5] 1, [13 18] 1, [6 10] 2, [3 8] 0, [9 4] 1, [18 15] 1, [7 14] 1, [14 2] 1, [0 11] 1, [6 12] 1, [16 1] 0, [14 16] 2, [16 15] 0, [4 13] 1, [1 6] 0, [2 14] 0, [16 9] 2, [14 3] 1, [4 4] 1, [11 17] 1, [7 16] 2, [3 7] 1, [2 10] 1, [7 5] 2, [7 15] 2, [13 10] 1, [2 6] 1, [16 10] 0, [5 0] 0, [6 2] 0, [14 12] 1, [17 9] 2, [9 10] 1, [8 14] 1, [8 12] 0, [13 4] 1, [6 0] 2, [7 17] 2, [16 4] 1, [15 15] 2, [12 16] 2, [14 7] 1, [1 2] 1, [14 10] 2, [6 16] 2, [5 18] 2, [15 14] 1, [2 16] 0, [10 3] 1, [18 13] 0, [3 5] 0, [13 14] 2, [0 8] 0, [5 12] 0, [3 2] 2, [17 8] 1, [14 9] 2, [18 5] 2, [11 16] 0, [17 17] 1, [14 0] 1, [0 1] 0, [15 2] 1, [8 17] 0, [4 0] 1, [17 7] 2}, :maxima [18 18]} {:perms {[0 0] 0, [0 1] 1, [1 1] 2, [0 2] 2}, :temps {}, :candidates {[0 0] 7, [0 1] 8, [0 2] 9}, :tool 2, :cave {[8 8] 0, [7 6] 2, [8 7] 0, [16 6] 2, [3 15] 2, [8 11] 1, [9 8] 1, [7 1] 2, [10 14] 1, [12 12] 1, [8 9] 2, [7 12] 0, [2 18] 0, [8 18] 2, [12 18] 0, [12 6] 1, [15 4] 0, [13 3] 1, [10 5] 2, [13 15] 0, [15 11] 2, [11 9] 2, [11 2] 0, [4 3] 0, [7 11] 2, [17 5] 1, [2 2] 1, [12 17] 0, [7 13] 0, [18 12] 2, [0 0] 0, [9 18] 0, [3 9] 1, [13 8] 2, [4 12] 1, [7 7] 1, [18 3] 2, [2 8] 0, [10 15] 2, [13 6] 0, [14 17] 1, [17 2] 2, [1 0] 1, [8 4] 0, [17 6] 2, [2 3] 2, [18 7] 0, [2 5] 2, [7 2] 0, [16 5] 2, [15 16] 0, [10 13] 1, [15 17] 2, [18 0] 0, [6 7] 2, [15 0] 0, [12 13] 0, [15 12] 1, [12 14] 2, [7 4] 0, [8 3] 0, [9 15] 2, [0 6] 2, [3 3] 0, [13 12] 2, [10 9] 1, [5 4] 1, [3 16] 2, [15 3] 0, [10 8] 2, [5 10] 1, [18 2] 2, [1 1] 2, [6 3] 0, [7 18] 1, [17 16] 2, [0 5] 1, [14 6] 0, [3 4] 0, [17 18] 1, [11 14] 2, [11 0] 2, [17 0] 1, [12 8] 2, [1 12] 0, [12 5] 0, [7 3] 2, [8 6] 2, [12 2] 0, [16 7] 1, [17 12] 1, [1 15] 2, [15 9] 1, [17 1] 1, [4 2] 0, [14 13] 1, [0 18] 1, [7 8] 0, [9 16] 2, [3 12] 2, [4 14] 2, [13 2] 2, [18 10] 1, [3 0] 2, [10 17] 1, [18 18] 0, [9 0] 1, [6 6] 1, [9 6] 2, [1 13] 2, [12 1] 1, [11 13] 0, [13 9] 0, [6 13] 0, [16 2] 0, [1 9] 1, [18 11] 2, [0 13] 2, [11 18] 0, [8 10] 1, [5 3] 0, [18 4] 2, [18 14] 1, [9 9] 2, [13 7] 1, [9 3] 1, [0 17] 0, [9 12] 1, [4 7] 1, [13 1] 0, [4 10] 2, [4 15] 1, [0 15] 2, [0 14] 2, [3 13] 2, [4 9] 1, [1 10] 1, [2 9] 1, [18 9] 1, [6 5] 1, [11 11] 0, [5 13] 0, [13 0] 2, [3 17] 2, [5 14] 2, [16 13] 1, [4 11] 0, [18 6] 2, [5 16] 0, [0 9] 0, [8 0] 2, [4 1] 1, [5 2] 0, [2 13] 1, [16 3] 0, [4 6] 1, [14 15] 2, [15 7] 1, [17 4] 1, [16 8] 2, [11 4] 2, [1 4] 2, [10 2] 1, [11 8] 0, [1 11] 2, [5 7] 2, [11 12] 1, [10 16] 1, [16 16] 2, [12 7] 2, [12 0] 1, [8 2] 2, [4 18] 2, [16 0] 2, [10 7] 1, [10 0] 0, [11 10] 2, [1 3] 0, [4 8] 1, [10 11] 2, [18 1] 1, [12 11] 2, [15 1] 0, [1 5] 2, [9 14] 0, [13 13] 2, [12 10] 2, [11 6] 0, [14 4] 0, [11 3] 0, [15 10] 1, [1 8] 1, [8 16] 2, [1 7] 0, [12 4] 1, [16 17] 2, [15 18] 2, [6 4] 0, [15 5] 1, [18 8] 0, [18 17] 0, [1 18] 0, [8 1] 0, [2 12] 1, [16 11] 2, [14 8] 2, [18 16] 1, [0 3] 1, [5 1] 2, [6 1] 2, [2 11] 2, [6 14] 1, [5 11] 0, [1 16] 2, [5 6] 1, [5 8] 1, [16 18] 0, [2 15] 0, [13 11] 2, [8 15] 0, [6 18] 0, [8 13] 2, [8 5] 2, [0 7] 2, [4 17] 0, [3 18] 0, [9 17] 0, [14 11] 2, [15 8] 0, [6 8] 1, [13 5] 0, [9 11] 1, [6 11] 0, [10 1] 2, [5 5] 0, [7 9] 2, [10 12] 2, [14 1] 0, [2 7] 0, [11 1] 0, [5 17] 2, [2 17] 2, [13 17] 1, [15 6] 2, [5 9] 1, [2 4] 0, [3 6] 0, [5 15] 1, [6 15] 0, [12 15] 1, [14 5] 2, [7 10] 1, [17 3] 1, [10 6] 0, [17 14] 0, [17 10] 0, [9 2] 2, [0 12] 1, [4 5] 1, [11 7] 1, [9 1] 0, [9 7] 2, [10 4] 0, [17 15] 2, [15 13] 0, [10 10] 2, [7 0] 0, [0 16] 0, [12 9] 1, [0 2] 0, [6 9] 1, [17 11] 2, [11 15] 1, [4 16] 0, [3 14] 1, [16 12] 2, [2 0] 0, [0 4] 1, [3 11] 2, [10 18] 1, [14 18] 2, [0 10] 0, [11 5] 1, [9 13] 1, [3 1] 2, [3 10] 1, [12 3] 2, [16 14] 1, [13 16] 2, [17 13] 0, [14 14] 1, [1 17] 1, [2 1] 1, [6 17] 1, [1 14] 1, [9 5] 1, [13 18] 1, [6 10] 2, [3 8] 0, [9 4] 1, [18 15] 1, [7 14] 1, [14 2] 1, [0 11] 1, [6 12] 1, [16 1] 0, [14 16] 2, [16 15] 0, [4 13] 1, [1 6] 0, [2 14] 0, [16 9] 2, [14 3] 1, [4 4] 1, [11 17] 1, [7 16] 2, [3 7] 1, [2 10] 1, [7 5] 2, [7 15] 2, [13 10] 1, [2 6] 1, [16 10] 0, [5 0] 0, [6 2] 0, [14 12] 1, [17 9] 2, [9 10] 1, [8 14] 1, [8 12] 0, [13 4] 1, [6 0] 2, [7 17] 2, [16 4] 1, [15 15] 2, [12 16] 2, [14 7] 1, [1 2] 1, [14 10] 2, [6 16] 2, [5 18] 2, [15 14] 1, [2 16] 0, [10 3] 1, [18 13] 0, [3 5] 0, [13 14] 2, [0 8] 0, [5 12] 0, [3 2] 2, [17 8] 1, [14 9] 2, [18 5] 2, [11 16] 0, [17 17] 1, [14 0] 1, [0 1] 0, [15 2] 1, [8 17] 0, [4 0] 1, [17 7] 2}, :maxima [18 18]}])
(expect t-state-00 (chr/spread t-state-0))

;(expect-focused "" (chr/work ex-target t-state-01))

(expect-focused 45 (chr/day22b t-state-0 ex-target))
;(expect-focused 1007 (chr/day22b state-0 target))
;(expect-focused t-state-at (dissoc (chr/spread state-0) :cave))

;1015 too high




)