# Unofficial Encyclopedia Metallum API

[![Build Status](https://travis-ci.org/Loki-Afro/metalarchives.svg?branch=develop)](https://travis-ci.org/Loki-Afro/metalarchives)

Unfortunately http://www.metal-archives.com/ does not provide any API whats so ever, this library exists.
Because Encyclopedia Metallum is still the best website to get correct and complete information about any metal band on earth.


## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Build

Just install it with maven
```
mvn clean install
```

## Howto

### Maven

Snapshots (Repository http://oss.sonatype.org/content/repositories/snapshots)

```xml
<dependency>
    <groupId>com.github.loki-afro</groupId>
    <artifactId>metalarchives-api</artifactId>
    <version>0.5.0</version>
</dependency>
```

### Gradle

```
compile 'com.github.loki-afro:metalarchives-api:0.5.0'
```

### Usage
```java
       public static void main(String[] args) {
                final BandSearchService service = new BandSearchService();
                final BandSearchQuery query = new BandSearchQuery();
 //             Here we give some parameters to the query
 //             In our case the name of the band and
 //             the second parameter here is exact match, because we are sure that there is a Band named Slayer
                query.setBandName("Slayer", true);
 //             now we say "search", like pressing the search button
                try {
                        final List<Band> resultList = service.performSearch(query);
                        for (final Band band : resultList) {
                                System.out.println("Bandname: " + band.getName());
                                System.out.println("Bandgenre: " + band.getGenre());
                                System.out.println("Bandstatus: " + band.getStatus().asString());
                                System.out.println("---");
                        }
                } catch (MetallumException e) {
                        e.printStackTrace();
                }
        }
```

## History
I wrote this library some years a ago, used it to tag my music library. (that tagger is not available and was just a huge hack :D)
Later it was used in an Android App to have a mobile version of the original site.

## License
```
"THE BEER-WARE LICENSE" (Revision 42):
Phillip Wirth<schaum@kaffeekrone.de>  wrote this file.
As long as you retain this notice you can do whatever you want with this stuff.
If we meet some day, and you think this stuff is worth it, you can buy me a non alcohol-free in return.
	Phillip Wirth
```
https://gist.github.com/Loki-Afro/f2162ba55e6ef22c670388d71698f2ce/538ba05a6d9665e109ed19743baf6646658992ba

