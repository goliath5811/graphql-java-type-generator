package graphql.java.generator;

import java.util.ArrayList;
import java.util.List;

public class ClassWithList {
    public List<Integer> integerList = new ArrayList<Integer>() {{
        add(0);
    }};

    public List<Integer> getIntegerList() {
        return integerList;
    }
}
