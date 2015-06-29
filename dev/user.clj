(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-scott-adts.core :refer [defadt match-adt]]))

(defadt Maybe
  (just a)
  (nothing))

(defn map-maybe
  [f ma]
  (maybe
   (comp just f)
   nothing
   ma))

(defn join-maybe
  [mma]
  (maybe
   identity
   nothing
   mma))
