
package com.jzb.org.service;

        import com.jzb.base.util.JzbCheckParam;
        import com.jzb.base.util.JzbRandom;
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
}

