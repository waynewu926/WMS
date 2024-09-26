package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.mapper.OutboundDetailMapper;
import com.yqdz.wms.model.domain.Outbound;
import com.yqdz.wms.model.domain.OutboundDetail;
import com.yqdz.wms.model.dto.OutboundCreateRequest;
import com.yqdz.wms.service.OutboundService;
import com.yqdz.wms.mapper.OutboundMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Galen
* @description 针对表【outbound(出库单)】的数据库操作Service实现
* @createDate 2024-06-26 14:32:02
*/
@Service
public class OutboundServiceImpl extends ServiceImpl<OutboundMapper, Outbound>
    implements OutboundService{
    @Resource
    private OutboundMapper outboundMapper;

    @Resource
    private OutboundDetailMapper outboundDetailMapper;

    @Override
    public boolean createOutbound(OutboundCreateRequest request) {
        //不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(request.getOutboundNum());
        if(matcher.find()){
            return false;
        }
        matcher = Pattern.compile(validPattern).matcher(request.getCustomer());
        if(matcher.find()){
            return false;
        }
        //不能重复
        QueryWrapper<Outbound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("outbound_num", request.getOutboundNum());
        long count = outboundMapper.selectCount(queryWrapper);
        if(count > 0){
            return false;
        }
        // 创建 Outbound 对象
        Outbound outbound = new Outbound();
        outbound.setOutboundNum(request.getOutboundNum());
        outbound.setCustomer(request.getCustomer());
        outbound.setOutboundStatus(2); // 默认状态未入库
        outbound.setCreatedBy("admin"); // 假设创建人为 admin
        outbound.setCreatedTime(new Date());
        outboundMapper.insert(outbound);

        // 创建 OutboundDetail 对象
        if(request.getOutboundDetail() != null && !request.getOutboundDetail().isEmpty()){
            for (OutboundCreateRequest.OutboundDetailRequest detailRequest : request.getOutboundDetail()) {
                OutboundDetail detail = getOutboundDetail(request, detailRequest);
                outboundDetailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    public List<Outbound> findOutbound(String outboundStatus, String outboundNum, String customer) {
        QueryWrapper<Outbound> queryWrapper = new QueryWrapper<>();

        if (outboundStatus != null && !outboundStatus.isEmpty()) {
            queryWrapper.eq("outbound_status", outboundStatus);
        }

        if (outboundNum != null && !outboundNum.isEmpty()) {
            queryWrapper.eq("outbound_num", outboundNum);
        }

        if (customer != null && !customer.isEmpty()) {
            queryWrapper.eq("customer", customer);
        }

        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteOutbound(Integer id) {
        // Fetch the outbound entry by ID
        Outbound outbound = this.getById(id);
        if (outbound != null) {
            // Delete the inbound details associated with the inbound entry
            QueryWrapper<OutboundDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("outbound_num", outbound.getOutboundNum());
            if (!queryWrapper.isEmptyOfWhere()) {
                outboundDetailMapper.delete(queryWrapper);
            }

            // Delete the inbound entry itself
            return this.removeById(id);
        }
        return false;
    }

    private static OutboundDetail getOutboundDetail(OutboundCreateRequest request, OutboundCreateRequest.OutboundDetailRequest detailRequest) {
        OutboundDetail detail = new OutboundDetail();
        detail.setOutboundNum(request.getOutboundNum());
        detail.setItemNum(detailRequest.getItemNum());
        detail.setPackagingCapacity(detailRequest.getPackagingCapacity());
        detail.setPlanQuantity(detailRequest.getPlanQuantity());
        detail.setRealQuantity(0); // 默认实际入库数量为 0
        detail.setItemStatus(2); // 默认状态未入库
        detail.setCreatedBy("admin"); // 假设创建人为 admin
        detail.setCreatedTime(new Date());
        return detail;
    }
}




