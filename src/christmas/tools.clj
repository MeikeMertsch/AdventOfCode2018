(ns christmas.tools
  (:require [clojure.string :as str]
            [expectations :refer :all]))

(def f-test-header "(ns christmas.chr%s.day%s-test
  (:require [christmas.chr%s.day%s :as chr]
  		  	[expectations :refer :all]
            [clojure.string :as str]))

(def file (slurp \"resources/chr%s/day%sreal\"))")

(def f-header "(ns christmas.chr%s.day%s
  (:require [clojure.string :as str]))")

(def f-path "src/christmas/chr%s/day%s.clj")
(def f-test-path "test/christmas/chr%s/day%s_test.clj")
(def f-res-path "resources/chr%s/day%sreal")

(defn create-string [string year day]
  (format string year day year day year day))

(defn create-file [file content]
  (spit file content))
	;(println file content))	

(defn prepare [year day]
  (create-file (create-string f-path year day) (create-string f-header year day))
  (create-file (create-string f-test-path year day) (create-string f-test-header year day))
  (create-file (create-string f-res-path year day) ""))

(defn parse-int
  ([string]	(parse-int string 10))
  ([string base]	(Integer/parseInt string base)))

(defn sqrt[number]
  (Math/sqrt number))

(defn map-kv [f coll]
  (reduce-kv (fn [m k v] (assoc m k (f v))) (empty coll) coll))

(defn llast [x]
  (last (last x)))

(defn flast [x]
  (first (last x)))

(defn lfirst [x]
  (last (first x)))

(defn second-last [x]
  (last (butlast x)))

(defn abs [n]
  (max n (- n)))

(defn manhatten [coords]
  (->> (map abs coords)
       (apply +)))

(defn find-first [f coll]
  (first (filter f coll)))

(defn add-to-last [vec-of-vecs new-value]
  (conj (pop vec-of-vecs) (conj (peek vec-of-vecs) new-value)))

(defn update-last [vec-of-vecs update-func]
  (conj (pop vec-of-vecs) (conj (pop (peek vec-of-vecs)) (update-func (peek (peek vec-of-vecs))))))


;(expect "" )
;(expect "" (map (partial prepare 16) (map (partial str "0") (range 1 10))))
;(expect "" (map (partial prepare 16) (range 10 26)))
;(expect nil (prepare "16" "25"))