# Unofficial Encyclopedia Metallum API

![Build Status](https://github.com/Loki-Afro/metalarchives//actions/workflows/main.yml/badge.svg?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.loki-afro/metalarchives-api/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.loki-afro/metalarchives-api)

Unfortunately http://www.metal-archives.com/ does not provide any API whats so ever, this library exists. Because
Encyclopedia Metallum is still the best website to get correct and complete information about any metal band on earth.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Build

Just build it with maven

```
mvn clean install
```

## Howto

### Maven

```xml
<dependency>
    <groupId>com.github.loki-afro</groupId>
    <artifactId>metalarchives-api</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle

```
compile 'com.github.loki-afro:metalarchives-api:1.0.1'
```

### Usage

```java
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.google.common.collect.Sets;

import java.util.List;

public class APIExample {
// unit tests are full of examples
    private bandExample() {

        // just using the search
        BandQuery simpleSearchQuery = BandQuery.builder()
                .country(Country.DE)
                .statuses(Sets.newHashSet(BandStatus.SPLIT_UP, BandStatus.ON_HOLD))
                .build();
        for (SearchBandResult result : API.getBands(build)) {
            System.out.println("BandName" + result.getName());
            System.out.println("BandId" + result.getId());
        }


        // or if you wanna go all in: search and load all bands by name
        BandQuery query = BandQuery.byName("Slayer", true);
        for (final Band fullBand : API.getBandsFully(query)) {
            System.out.println("Bandname: " + fullBand.getName());
            System.out.println("Bandgenre: " + fullBand.getGenre());
            System.out.println("Bandstatus: " + fullBand.getStatus());
            System.out.println("Partial Discs: " + fullBand.getDiscsPartial());
//          or if you want the discs with all details
            System.out.println("Discs: " + fullBand.getDiscs());
            System.out.println("---");
        }

        // get by id
        Band band = API.getBandById(666L);
        System.out.println(band.getCountry());

    }

    public discExample() {
//        same with band we can do the default operations like searching and getting a single disc by its id
        Disc disc = API.getDiscById(845L);
        for (Track track : disc.getTrackList()) {
            System.out.println("name " + track.getPlayTime());
            System.out.println("playtime " + track.getPlayTime());
//            ...
        }
        
//        you can also load the lyrics of the tracks
        DiscSearchService service = new DiscSearchService();
        service.setLoadLyrics(true);

        DiscQuery query = DiscQuery.builder()
                .name("Hordanes Land")
                .discType(DiscType.EP)
                .build();
//        enforces that you get only one result, if there are more than one an exception will be thrown
        final Disc discResult = service.getSingleUniqueByQuery(query);
        
//        or you might be interested in reviews + artworks + lyrics?
        DiscQuery query = DiscQuery.builder()
                .name("The Wretched Spawn")
                .build();
        final Disc discResult = new DiscSearchService(true, true, true).getById(query);
        System.out.println(convertToAsciiArtNotIncluded(discResult.getArtwork()));
        for (Review review : discResult.getReviews()) {
            System.out.println(review.getAuthor());
            System.out.println(review.getDate());
            System.out.println(review.getContent());
//            ...
        }
    }

    public trackExample() {
//        getting the lyrics of a single track is a bit tricky
//        There is no dedicated "Track Webpage", but its not impossible ..
        
//        this query results, as of January 2021 to one result 
        TrackQuery query = TrackQuery.builder()
                .name("Fatal Self-Inflicted Disfigurement")
                .exactNameMatch(true)
                .discType(DiscType.FULL_LENGTH)
                .build();

//        loading lyrics is activated here
        Track track = new TrackSearchService(true).getFully(query).iterator().next();;

        System.out.println(track.getLyrics());
    }
}
```

## Background

I wrote this library almost 10 years a ago, used it to tag my music library. (that tagger is not available and was just a
huge hack)
Later it was used in an Android App to have a mobile version of the original site.

It was used for some papers to analyzed lyrics
Several music players back in the day displayed some information by using this api

## License

```
"THE BEER-WARE LICENSE" (Revision 42):
Phillip Wirth<schaum@kaffeekrone.de>  wrote this file.
As long as you retain this notice you can do whatever you want with this stuff.
If we meet some day, and you think this stuff is worth it, you can buy me a non alcohol-free in return.
	Phillip Wirth
```

https://gist.github.com/Loki-Afro/f2162ba55e6ef22c670388d71698f2ce/538ba05a6d9665e109ed19743baf6646658992ba

