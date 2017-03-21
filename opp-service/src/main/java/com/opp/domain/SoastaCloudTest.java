package com.opp.domain;

/**
 * Created by jhermida on 10/10/16.
 */
public class SoastaCloudTest extends LoadTest {
    private String ctComposition;
    private String testDataType;
    private String dataFile;

    public String getCtComposition() {
        return ctComposition;
    }

    public void setCtComposition(String ctComposition) {
        this.ctComposition = ctComposition;
    }

    public String getTestDataType() {
        return testDataType;
    }

    public void setTestDataType(String testDataType) {
        this.testDataType = testDataType;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public String toString() {
        return "SoastaCloudTest{" +
                "ctComposition='" + ctComposition + '\'' +
                ", testDataType='" + testDataType + '\'' +
                ", dataFile='" + dataFile + '\'' +
                super.toString() +
                '}';
    }
}
