(ns clj-mogensen-scott.core
  (:require [camel-snake-kebab.core :refer [->kebab-case]]
            [potemkin.macros :refer [unify-gensyms]]))

(def ^:no-doc sym->kw (comp keyword name))

(defprotocol ADT
  (represent-tagged-vector [adt]))

(defn ^:no-doc tagged-vector
  [[ctor-name & fields]]
  (if (seq fields)
    `(partial vector ~(sym->kw ctor-name))
    [(sym->kw ctor-name)]))

(defn ^:no-doc gen-type
  [type-name ctors]
  `(deftype ~type-name [~'f]
     ADT
     (represent-tagged-vector
       [adt#]
       (~(->kebab-case type-name) ~@(map tagged-vector ctors) adt#))))

(defn ^:no-doc gen-adt-fold
  [ctor-names type-name]
  `(defn ~(->kebab-case type-name)
     [~@ctor-names val#]
     ((.f val#) ~@ctor-names)))

(defn ^:no-doc gen-adt-ctor
  [type-ctor ctor-names [ctor & fields]]
  (if (seq fields)
    `(defn ~ctor
       [~@fields]
       (~type-ctor (fn [~@ctor-names] (~ctor ~@fields))))
    `(def ~ctor (~type-ctor (fn [~@ctor-names] ~ctor)))))

(defn ^:no-doc defadt*
  [type-name ctors]
  (let [ctor-names (map first ctors)
        type-ctor (symbol (str "->" (name type-name)))]
    `(do
       (declare ~(->kebab-case type-name))
       ~(gen-type type-name ctors)
       ~(gen-adt-fold ctor-names type-name)
       ~@(map (partial gen-adt-ctor type-ctor ctor-names) ctors))))

(defmacro defadt
  "Define an ADT with the specified constructors. "
  [name & ctors]
  (defadt* name ctors))

(defn ^:no-doc match-case
  [[[destr & binders] expr]]
  (if binders
    `(~(sym->kw destr)
      (let [[~@binders] fields##]
        ~expr))
    `(~(sym->kw destr)
      ~expr)))

(defn ^:no-doc match-adt*
  [on cases default]
  (unify-gensyms
   `(let [[tag# & fields##] (represent-tagged-vector ~on)]
      (case tag#
        ~@(mapcat match-case cases)
        ~default))))

(defmacro match-adt
  "Pattern match on ADT constructors. Optionally specify a default value if no constructors match."
  [on & cs]
  (let [[cases default] (if (odd? (count cs))
                          ((juxt butlast (comp first last)) (partition-all 2 cs))
                          [(partition 2 cs) '(throw (Exception. "No default case"))])]
    (match-adt* on cases default)))
