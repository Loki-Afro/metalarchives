package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.API;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberSearchExampleTest {

    @Disabled
    public void test() throws MetallumException {

        List<String> stringList = new ArrayList<>();
        stringList.add("Kerry King");
        stringList.add("");
        stringList.add("Fenriz");
        stringList.add("Bruce Dickinson");
        stringList.add("Mick Kenney");
        stringList.add("Joel Lindholm");
        stringList.add("Varg Vikernes");
        for (String memberName : stringList) {
            // System.out.println("searching with: " + memberName);
            assertThat(API.getMemberByName(memberName)).isNotEmpty();
        }

    }
}
