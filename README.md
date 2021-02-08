# ClojureWerkz Money / r:s branch
## A Clojure Library to Work With Money

ClojureWerkz Money is a Clojure library that deals with monetary amounts.
It is built on top of [Joda Money](http://joda-money.sourceforge.net/).

This is random:seed branch of this library.

## Project Goals

 * Expose most or all Joda Money features in an easy to use way
 * Be well documented and well tested
 * Integrate with popular libraries such as [Cheshire](https://github.com/dakrone/cheshire) and [Monger](http://clojuremongodb.info)
 * Don't introduce any significant amount of performance overhead


## Maven Artifacts

Money artifacts are [released to Clojars](https://clojars.org/io.randomseed/money). If you are using Maven, add the following repository
definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```

### Most Recent Release

With Leiningen:

    [io.randomseed/money "1.0.1-0"]


With Maven:

    <dependency>
      <groupId>io.randomseed</groupId>
      <artifactId>money</artifactId>
      <version>1.0.1-0</version>
    </dependency>


## Documentation

### Monetary Amounts

Monetary amounts are instantiated using `io.randomseed.money.amounts` functions. They operate on
floating point amounts (doubles) or long values in major units (e.g. dollars) or minor units (e.g. cents).

Note that some currencies do not have minor units (most notably `JPY`). For those, use `io.randomseed.money.amounts/of-major`.

``` clojure
(require '[io.randomseed.money.amounts :as ma])
(require '[io.randomseed.money.currencies :as mc])

;; USD 10.50
(ma/amount-of mc/USD 10.5)
;; USD 10
(ma/of-major mc/USD 10)
;; USD 10.50
(ma/of-minor mc/USD 1050)

;; JPY 1000
(ma/of-major mc/JPY 1000)
```

Note that not all currencies have minor units (most notably JPY does not).

It is possible to parse a string in the standard format `[currency unit] [amount]`, e.g. `JPY 1000`:

``` clojure
(require '[io.randomseed.money.amounts :as ma])

(ma/parse "JPY 1000")
;= org.joda.money.Money instance for JPY 1000
```

Monetary amounts can be added, substracted and so on using `io.randomseed.money.amounts/plus`,
`io.randomseed.money.amounts/minus`, `io.randomseed.money.amounts/multiply`, and
`io.randomseed.money.amounts/divide` functions:

``` clojure
(require '[io.randomseed.money.amounts    :as ma])
(require '[io.randomseed.money.currencies :as mc])

(ma/plus (ma/amount-of mc/USD 10) (ma/amount-of mc/USD 100))
;= USD 110

(ma/minus (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 10))
;= USD 90

(ma/multiply (ma/amount-of mc/USD 100) 10)
;= USD 1000

;; :floor for flooring round mode
(ma/divide (ma/amount-of mc/USD 100.1) 10 :floor)
;= USD 10
```

It is possible to add up all monies in a collection or sequence using `io.randomseed.money.amounts/total`:

``` clojure
(require '[io.randomseed.money.amounts    :as ma])
(require '[io.randomseed.money.currencies :as mc])

(ma/total [(ma/amount-of mc/USD 10) (ma/amount-of mc/USD 100)])
;= USD 110
```

It is possible to compare monetary amounts using >, >=, < and <=.

```clojure
(require '[io.randomseed.money.amounts    :as ma])
(require '[io.randomseed.money.currencies :as mc])

(ma/< (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 100))
;= false

(ma/<= (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 120))
;= true

(ma/>= (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 100) (ma/amount-of mc/USD 120))
;= false

(ma/> (ma/amount-of mc/USD 200) (ma/amount-of mc/USD 100))
;= true
```

### Rounding

`io.randomseed.money.amounts/round` is a function that performs rounding of
monetary values using one of the rounding modes:

``` clojure
(require '[io.randomseed.money.amounts :as ams])

(ams/round (ams/amount-of cu/USD 40.01) -1 :floor)
;= USD 40

(ams/round (ams/amount-of cu/USD 40.01) -1 :up)
;= USD 50

(ams/round (ams/amount-of cu/USD 45.24) 0 :floor)
;= USD 45

(ams/round (ams/amount-of cu/USD 45.24) 0 :up)
;= USD 46

(ams/round (ams/amount-of cu/USD 45.24) 1 :floor)
;= USD 45.20

(ams/round (ams/amount-of cu/USD 45.24) 1 :up)
;= USD 45.30
```


### Currencies

Currency units use their ISO-4217 codes and represented by `org.joda.money.CurrencyUnit` instances.
Usually the easiest way to use currency units is via `io.randomseed.money.currencies` aliases:

``` clojure
(require '[io.randomseed.money.currencies :as mc])

mc/USD ;= USD currency unit
mc/CAD ;= CAD currency unit
mc/GBP ;= GBP currency unit
mc/RUB ;= RUB currency unit
```

`io.randomseed.money.currencies/for-code` and `io.randomseed.money.currencies/of-country` can be used
to get currency units by their ISO-4217 code strings and country abbreviations:

``` clojure
(require '[io.randomseed.money.currencies :as mc])

(mc/for-code "CHF")   ;= CHF currency unit
(mc/for-country "CH") ;= CHF currency unit
```

`io.randomseed.money.currencies/pseudo-currency?` is a predicate function that takes a currency unit
and returns true if it is a pseudo-currency (e.g. [Bitcoin](http://bitcoin.org) or [IMF Special Drawing Rights](http://www.imf.org/external/np/exr/facts/sdr.htm)).


### Currency Conversion

`io.randomseed.money.amounts/convert-to` converts a monetary value in one currency
to another using provided exchange rate and rounding mode:

``` clojure
(require '[io.randomseed.money.amounts :as ams])

(ams/convert-to (ams/amount-of cu/GBP 65.65) cu/USD 1.523 :down)
;= USD 99.98
```


### Formatting

Money supports formatting of monetary amounts with the `io.randomseed.money.format/format` function
which takes an amount and (optionally) a locale and a formatter:

``` clojure
(require '[io.randomseed.money.currencies :as cu])
(require '[io.randomseed.money.amounts :refer [amount-of]])
(require '[io.randomseed.money.format :refer :all])

(import java.util.Locale)

;; format using default system locale
(format (amount-of cu/GBP 20.0)) ;= GBP20,00
;; format using UK locale
(format (amount-of cu/GBP 20.0) Locale/UK) ;= £20.00

;; format using Brazilian locale
(format (amount-of cu/BRL 20.0) (Locale. "pt" "BR")) ;= R$20,00
```

Default formatter uses localized currency symbol and amount and default locale which JVM infers from environment
settings.

### Cheshire Integration

`io.randomseed.money.json`, when loaded, registers serializers for
`org.joda.money.Money` and `org.joda.money.CurrencyUnit` with
Cheshire. Serialization conventions used are straightforward and
produce human readable values:

 * `(io.randomseed.money.currencies/USD)` => `"USD"`
 * `(io.randomseed.money.amounts/amount-of (io.randomseed.money.currencies/USD) 20.5)` => `"USD20.50"` (will use system locale by default)

To use it, simply require the namespace and then use Cheshire
generation functions as usual.

This extension requires Cheshire `5.0.x` or later. `clojure.data.json`
is not supported.


### Monger Integration

`io.randomseed.money.monger`, when loaded, registers BSON serializers
for `org.joda.money.Money` and
`org.joda.money.CurrencyUnit`. Serialization conventions used are
straightforward and produce human readable values:

 * `(io.randomseed.money.currencies/USD)` => `"USD"`
 * `(io.randomseed.money.amounts/amount-of (io.randomseed.money.currencies/USD) 20.5)` => `{"currency-unit" "USD" "amount-in-minor-units" 2050}`

Note that serialization is one-way: loaded documents are returned as
maps because there is no way to tell them from regular BSON
documents. `io.randomseed.money.monger/from-stored-map` can be used to
produce `Money` instances from maps following the serialization
convention described above.


### Hiccup Integration

`io.randomseed.money.hiccup`, when loaded, extends [Hiccup](https://github.com/weavejester/hiccup) HTML rendering protocol to render
monetary amounts and currency units.
Rendering conventions used are straightforward and
produce human readable values:

 * `(io.randomseed.money.currencies/USD)` => `"USD"`
 * `(io.randomseed.money.amounts/amount-of (io.randomseed.money.currencies/USD) 20.5)` => `"USD20.50"` (will use system locale by default)

To use it, simply require the namespace and then use Hiccup
as usual.


## Community

To get announcements of releases, important changes and so on, please follow [@therandomseed](https://twitter.com/#!/therandomseed) on Twitter.


## Supported Clojure Versions

ClojureWerkz Money R:S branch is built from the ground up for Clojure 1.10 and up.
The most recent release is always recommended.


## Continuous Integration

[![Continuous Integration status](https://circleci.com/gh/randomseed-io/money.svg?style=svg)](https://circleci.com/gh/randomseed-io/money)

CI is hosted by [CircleCI](https://cicrleci.com)

## Development

Money uses [deps.edn](https://clojure.org/guides/deps_and_cli).

## License

Copyright © 2021 Paweł Wilk and the random:seed team.  
Copyright © 2012-2016 Michael S. Klishin, Alex Petrov, and the ClojureWerkz team.

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or
the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
