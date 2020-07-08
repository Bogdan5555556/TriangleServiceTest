package com.apitest.matchers;

import com.apitest.dataModel.TriangleResponseModel;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class EqualTriangleMatcher extends TypeSafeMatcher<TriangleResponseModel> {

    private Double firstSide;
    private Double secondSide;
    private Double thirdSide;

    EqualTriangleMatcher(Double firstSide, Double secondSide, Double thirdSide) {
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("FirstSide - %2f, SecondSide - %2f, ThirdSide - %2f", firstSide, secondSide, thirdSide));
    }

    @Override
    protected boolean matchesSafely(TriangleResponseModel triangleGetModel) {
        return triangleGetModel.getFirstSide().equals(firstSide) &&
                triangleGetModel.getSecondSide().equals(secondSide) &&
                triangleGetModel.getThirdSide().equals(thirdSide);
    }

    public static EqualTriangleMatcher triangleHasDimensions(Double firstSide, Double secondSide, Double thirdSide) {
        return new EqualTriangleMatcher(firstSide, secondSide, thirdSide);
    }
}
