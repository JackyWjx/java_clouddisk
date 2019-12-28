package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/5 16:56
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/dept/user")
public interface TbDeptUserListApi {

    /**
     * @Author sapientia
     * @Date 16:57 2019/12/5
     * @Description 根据部门id查询部门下的人员
     **/
    @RequestMapping(value = "/queryUsernameBydept", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUsernameBydept(@RequestBody Map<String, Object> param);

    /**
     * @Author sapientia
     * @Date 16:49 2019/12/11
     * @Description 查询同公司下的其他人
     **/
    @RequestMapping(value = "/queryOtherPersonByuid", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryOtherPersonBycid(@RequestBody Map<String, Object> param);


    /**
     * @Author sapientia
     * @Date 18:20 2019/12/12
     * @Description 查询审批人名
     **/
    @RequestMapping(value = "/queryPersonNameByuid", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryPersonNameByuid(@RequestBody Map<String, Object> param);

    /**
     *  @author: gongWei
     *  @Date:  2019/12/13 10:18
     *  @Description: 查询同行人名
     *  @Param:
     *  @Return:
     *  @Exception:
     */
    @RequestMapping(value = "/getUsernameList", method = RequestMethod.POST)
    @CrossOrigin
    Response searchInvitee(@RequestBody Map<String, Object> param);
}
