package com.opp.service;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SoastaCloudTestServiceTest {


    private SoastaCloudTestService soastaCloudTestService = new SoastaCloudTestService();

    @Ignore
    @Test
    public void exportResults() {
        String s = "/Projects/UI/CampaignUI/CampaignUI_SSS_Flow/S1_CampaignUI_SSS_Flow";
        String collectionDetails = "resultcollectiondetails";

        soastaCloudTestService.exportResults(s, collectionDetails, null);
    }

}
