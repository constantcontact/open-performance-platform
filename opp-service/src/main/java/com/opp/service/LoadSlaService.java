package com.opp.service;

import com.opp.dao.LoadSlaDao;
import com.opp.domain.LoadSla;
import com.opp.domain.LoadSlaGroup;
import com.opp.domain.LoadSlaTestGroup;
import com.opp.dto.LoadTestSla;
import com.opp.dto.LoadTestSlaResult;
import com.opp.exception.BadRequestException;
import com.opp.exception.InternalServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ctobe on 8/8/16.
 */
@Service
public class LoadSlaService {

    @Autowired
    private LoadSlaDao dao;

    @Autowired
    private LoadSlaGroupService loadSlaGroupService;

    @Autowired
    private LoadSlaTestGroupService loadSlaTestGroupService;

    public Optional<LoadSla> add(LoadSla sla) {
        int insertId = dao.insert(sla);
        return dao.findById(insertId);
    }

    public Optional<LoadSla> update(Integer slaId, LoadSla update) {
        dao.update(slaId, update);
        return dao.findById(slaId);
    }

    public boolean delete(Integer slaGroupId) {
        return (dao.delete(slaGroupId) == 1);
    }

    public Optional<LoadSla> getById(Integer slaId) {
        return dao.findById(slaId);
    }

    public List<LoadSla> getAll() {
        return dao.findAll();
    }

    public List<LoadTestSla> getAllByLoadTestId(Integer loadTestId) {
        return dao.findAllByLoadTestId(loadTestId);
    }

// PHP code to convert still
//    public function getSLAResults($id){
//        $query = "  select
//        lsg.id groupId, lsg.name groupName, ls.*, lstg.load_test_id, lta.*,
//                (lta.resp_min - ls.min) as min_delta, ((lta.resp_min - ls.min)/lta.resp_min)*100 as min_delta_pt,
//        (lta.resp_max - ls.max) as max_delta, ((lta.resp_max - ls.max)/lta.resp_max)*100 as max_delta_pt,
//        (lta.resp_avg - ls.avg) as avg_delta, ((lta.resp_avg - ls.avg)/lta.resp_avg)*100 as avg_delta_pt,
//        (lta.resp_median - ls.median) as median_delta, ((lta.resp_median - ls.median)/lta.resp_median)*100 as median_delta_pt,
//        (lta.resp_pct75 - ls.pct75) as pct75_delta, ((lta.resp_pct75 - ls.pct75)/lta.resp_pct75)*100 as pct75_delta_pt,
//        (lta.resp_pct90 - ls.pct90) as pct90_delta, ((lta.resp_pct90 - ls.pct90)/lta.resp_pct90)*100 as pct90_delta_pt
//        from load_sla_group lsg, load_sla ls, load_sla_test_group lstg, load_test_aggregate lta
//        where lstg.load_test_id = $id
//        and lsg.id = lstg.load_sla_group_id
//        and lstg.is_active = true
//        and ls.load_sla_group_id = lsg.id
//        and ls.name = lta.transaction_name
//        and lta.load_test_id = lstg.load_test_id";
//        $res = $this->db->query($query)->result();
//        $overallPassed = true;
//        $failures = array();
//        $warnings = array();
//        if(count($res) > 0){
//            // check these objects to determine of the SLA passed or failed.
//            $deltasToCheck = array('min_delta_pt', 'max_delta_pt', 'avg_delta_pt', 'median_delta_pt', 'pct75_delta_pt', 'pct90_delta_pt');
//            // loop through resultset
//            for($i=0; $i< count($res); $i++){
//                // get marginOfError
//                $marginOfError = $res[$i]->margin_of_error;
//                if(empty($marginOfError)) $marginOfError = 0;
//                // loop through each delta and check to see if it's greater than the SLA
//                foreach($deltasToCheck as $deltaName) {
//                    // create string like "pct90_pass" from "pct90_delta_pt"
//                    $passFailName = str_replace("delta_pt", "passed", $deltaName);
//                    // if we have a value
//                    if (!empty($res[$i]->$deltaName) && $res[$i]->$deltaName !== 0) {
//                        $slaType = str_replace("_delta_pt", "", $deltaName);
//                        $slas[] = array('name'=>$res[$i]->name, 'value'=>$res[$i]->$slaType, 'marginOfError'=>$marginOfError/100);
//                        // value set, check to see if the percent difference is greater than the margin of error
//                        if ($res[$i]->$deltaName > $marginOfError) {
//                            // set pass/fail object to true or false
//                            $res[$i]->$passFailName = false;
//                            $overallPassed = false; // fail the test
//                            $actValName = 'resp_' . $slaType;
//                            $failures[] = array('type'=>ucfirst($slaType), 'name'=>$res[$i]->name, 'msg'=>'The ' . ucfirst($slaType) . ' value of ' . $res[$i]->$actValName . 'ms was greater than the SLA of ' . $res[$i]->$slaType . 'ms by a margin of error greater than ' . $marginOfError . '%');
//                        }
//                    } else {
//                        // pass because we have no SLA
//                        $res[$i]->$passFailName = true;
//                    }
//                }
//                // check standard deviation
//                $stdDevWarnAt = $res[$i]->resp_avg * .5;
//                $stdDevErrAt = $res[$i]->resp_avg;
//                if($res[$i]->resp_stddev > $stdDevWarnAt){
//                    if($res[$i]->resp_stddev > $stdDevErrAt){
//                        // greater than the mean
//                        $warnings[] = array('type'=>'stddev', 'transaction'=>$res[$i]->transaction_name, 'severity'=>'high', 'msg'=>'Your standard deviation is very high at '. $res[$i]->resp_stddev .'ms and is greater than the mean (' . $res[$i]->resp_avg . 'ms).  Your test may be statistically invalid');
//                    } else {
//                        // greater than 1/2 the mean
//                        $warnings[] = array('type'=>'stddev', 'transaction'=>$res[$i]->transaction_name, 'severity'=>'medium', 'msg'=>'Your standard deviation of '. $res[$i]->resp_stddev .'ms is outside the norm and is greater than the 1/2 the mean (' . $res[$i]->resp_avg . 'ms).  Your test may be statistically invalid');
//                    }
//                }
//            }
//            return array('passed'=>$overallPassed, 'total_transactions'=> count($res), 'total_slas'=> count($slas), 'error_count'=> count($failures), 'errors'=>$failures, 'warning_count'=>count($warnings), 'warnings'=>$warnings, 'slas'=>$slas, 'raw_data'=>$res);
//        } else {
//            // return nothing
//            return array('passed'=>true, 'total_transactions'=> 0, 'total_slas'=> 0, 'error_count'=> 0, 'errors'=>array(), 'warning_count'=>0, 'warnings'=>array(), 'slas'=>array(), 'raw_data'=>array());
//        }
//    }




    /**
     * Bulk import SLAs.  A group id will be created.
     *
     * @param loadTestId
     * @param slas
     * @return
     */
    public LoadSlaBulkImportResult bulkImport(Integer loadTestId, List<LoadSla> slas) {
        LoadSlaBulkImportResult res = new LoadSlaBulkImportResult();

        if(slas.size() == 0) throw new BadRequestException("No or invalid SLAs sent");

        // create load test group id
        int slaGroupId = loadSlaGroupService.add(new LoadSlaGroup("Auto Generated Group from Load Test #" + loadTestId))
                .orElseThrow(() -> new InternalServiceException("Unable to create SLA Group")).getId();

        // add load test to SLA group
        loadSlaTestGroupService.insert(new LoadSlaTestGroup(0, ZonedDateTime.now(ZoneOffset.UTC), true, slaGroupId, loadTestId));// ignore the 0

        res.setTotal(slas.size());
        AtomicInteger successful = new AtomicInteger(0);
        slas.forEach(sla -> {
                sla.setLoadSlaGroupId(slaGroupId);
                add(sla).ifPresent(c -> successful.incrementAndGet());
            }
        );
        res.setSuccessful(successful.get());
        return res;
    }

    public LoadSlaBulkImportResult bulkUpdate(List<LoadSla> slas) {
        LoadSlaBulkImportResult res = new LoadSlaBulkImportResult();
        res.setTotal(slas.size());
        AtomicInteger successful = new AtomicInteger(0);
        slas.forEach(sla -> update(sla.getId(), sla).ifPresent(c -> successful.incrementAndGet()));
        res.setSuccessful(successful.get());
        return res;
    }

    public LoadTestSlaResult getLoadTestSlaResults(Integer loadTestId) {
        return new LoadTestSlaResult();
    }

    public static class LoadSlaBulkImportResult {
        private int total;
        private int successful;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSuccessful() {
            return successful;
        }

        public void setSuccessful(int successful) {
            this.successful = successful;
        }
    }
}
