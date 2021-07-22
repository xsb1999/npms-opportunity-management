package com.neu.opportunitymanagement.oppManagement.service.impl;

import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.dto.tracklog.UpdateTrackInfo;
import com.neu.opportunitymanagement.oppManagement.entity.Trackinglog;
import com.neu.opportunitymanagement.oppManagement.mapper.TrackinglogMapper;
import com.neu.opportunitymanagement.oppManagement.service.ITrackinglogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
@Service
public class TrackinglogServiceImpl extends ServiceImpl<TrackinglogMapper, Trackinglog> implements ITrackinglogService {

    @Autowired
    TrackinglogMapper trackinglogMapper;


    @Override
    @Transactional(rollbackFor=Exception.class)
    public RespBean curdTrackinglog(UpdateTrackInfo updateTrackInfo) {
        String oppId = updateTrackInfo.getOppId();
        List<Trackinglog> updateTrackList = updateTrackInfo.getUpdateTrackList();
        List<String> deletedTrackId = updateTrackInfo.getDeletedTrackId();
        // 更新机会跟踪日志表
        try {
            // 删除跟踪记录
            if (!deletedTrackId.isEmpty()){
                trackinglogMapper.deleteBatchIds(deletedTrackId);
            }
            for (Trackinglog t : updateTrackList) {
                // insert
                if (t.gettId() == null){
                    t.settOppId(oppId);
                    trackinglogMapper.insert(t);
                }
                // update
                else {
                    trackinglogMapper.updateById(t);
                }
            }
        }catch (Exception e){
            System.out.println("update fail!");
            throw e;
        }

        return RespBean.ok(200, "机会跟踪记录更新成功！");
    }
}
