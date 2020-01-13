
package com.jzb.org.service;

        import com.jzb.org.dao.TbPlantaskMapper;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Map;

/**
 * @author lifei
 */
@Service
public class TbPlantaskService {

    @Autowired
    private TbPlantaskMapper tbPlantaskMapper;

    public List<Map<String, Object>> getPantaskList1(Map<String, Object> param) {
        return tbPlantaskMapper.getPantaskList1(param);
    }



    public int addPlantaskBrother(Map<String, Object> param) {
        return tbPlantaskMapper.addPlantaskBrother(param);
    }


    public int updatePlantask(Map<String, Object> param) {
        return tbPlantaskMapper.updatePlantask(param);
    }

    public int getPantaskCount(Map<String, Object> param) {
        return tbPlantaskMapper.getPantaskCount(param);
    }

    public List<Map<String, Object>> getCompanydeptMap() {
        return tbPlantaskMapper.getCompanydeptMap();
    }

    public int delPlantask(Map<String, Object> param) {
        return tbPlantaskMapper.delPlantask(param);
    }

    public List<Map<String, Object>> selPantaskids(Map<String, Object> param) {
        return tbPlantaskMapper.selPantaskids(param);
    }

    public List<Map<String, Object>> getPantaskDas(Map<String, Object> param) {
        return tbPlantaskMapper.getPantaskDas(param);
    }

    public Map<String, Object> getPantaskDasXiangxi(Map<String, Object> param) {
        return tbPlantaskMapper.getPantaskDasXiangxi(param);
    }

    public int upDayXiangxi(Map<String, Object> param) {
        return tbPlantaskMapper.upDayXiangxi(param);
    }

    public List<Map<String, Object>> selcdids(String uid) {
        return tbPlantaskMapper.selcdids(uid);
    }

    public List<Map<String, Object>> selFu(Map<String, Object> param) {
        return tbPlantaskMapper.selFu(param);
    }

    public int upDayzhongzhi(Map<String, Object> param) {
        return tbPlantaskMapper.upDayzhongzhi(param);
    }
}

