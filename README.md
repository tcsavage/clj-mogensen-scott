# clj-mogensen-scott

ADTs in Clojure using Mogensen-Scott encoding.

## Usage

```
(require '[clj-mogensen-scott.core :refer [defadt match-adt]])

(defadt Maybe
  (just a)
  (nothing))

(defn map-maybe
  [ma f]
  (match-adt ma
    (just a) (just (f a))
    ma))
```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
