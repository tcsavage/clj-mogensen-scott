(ns clj-mogensen-scott.core
  (:require [camel-snake-kebab.core :refer [->kebab-case]]
            [potemkin.macros :refer [unify-gensyms]]))

(def sym->kw (comp keyword name))

(defprotocol ADT
  (represent-tagged-vector [adt]))

(defn tagged-vector
  [[ctor-name & fields]]
  (if (seq fields)
    `(partial vector ~(sym->kw ctor-name))
    [(sym->kw ctor-name)]))

(defn gen-type
  [type-name ctors]
  `(deftype ~type-name [~'f]
     ADT
     (represent-tagged-vector
       [adt#]
       (~(->kebab-case type-name) ~@(map tagged-vector ctors) adt#))))

(defn gen-adt-fold
  [ctor-names type-name]
  `(defn ~(->kebab-case type-name)
     [~@ctor-names val#]
     ((.f val#) ~@ctor-names)))

(defn gen-adt-ctor
  [type-ctor ctor-names [ctor & fields]]
  (if (seq fields)
    `(defn ~ctor
       [~@fields]
       (~type-ctor (fn [~@ctor-names] (~ctor ~@fields))))
    `(def ~ctor (~type-ctor (fn [~@ctor-names] ~ctor)))))

(defn defadt*
  [type-name ctors]
  (let [ctor-names (map first ctors)
        type-ctor (symbol (str "->" (name type-name)))]
    `(do
       (declare ~(->kebab-case type-name))
       ~(gen-type type-name ctors)
       ~(gen-adt-fold ctor-names type-name)
       ~@(map (partial gen-adt-ctor type-ctor ctor-names) ctors))))

(defmacro defadt
  [name & ctors]
  (defadt* name ctors))

(defn match-case
  [[[destr & binders] expr]]
  (if binders
    `(~(sym->kw destr)
      (let [[~@binders] fields##]
        ~expr))
    `(~(sym->kw destr)
      ~expr)))

(defn match-adt*
  [on cases default]
  (unify-gensyms
   `(let [[tag# & fields##] (represent-tagged-vector ~on)]
      (case tag#
        ~@(mapcat match-case cases)
        ~default))))

(defmacro match-adt
  [on & cs]
  (let [[cases default] (if (odd? (count cs))
                          ((juxt butlast (comp first last)) (partition-all 2 cs))
                          [(partition 2 cs) '(throw (Exception. "No default case"))])]
    (match-adt* on cases default)))
