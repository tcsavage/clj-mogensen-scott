# clj-scott-adts

ADTs in Clojure using Scott encoding.

## Usage

```
(require '[clj-scott-adts.core :refer [defadt match-adt]])

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

Copyright © 2015 Tom Savage

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
