package com.yqdz.wms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqdz.wms.model.dto.InboundCreateRequest;
import com.yqdz.wms.mapper.InboundDetailMapper;
import com.yqdz.wms.model.domain.Inbound;
import com.yqdz.wms.model.domain.InboundDetail;
import com.yqdz.wms.service.InboundService;
import com.yqdz.wms.mapper.InboundMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Galen
* @description 针对表【inbound(入库单)】的数据库操作Service实现
* @createDate 2024-06-21 13:09:11
*/
@Service
public class InboundServiceImpl extends ServiceImpl<InboundMapper, Inbound>
    implements InboundService {

    @Resource
    private InboundMapper inboundMapper;
    @Resource
    private InboundDetailMapper inboundDetailMapper;

    @Override
    public boolean createInbound(InboundCreateRequest request) {
        //不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(request.getInboundNum());
        if(matcher.find()){
            return false;
        }
        matcher = Pattern.compile(validPattern).matcher(request.getSupplier());
        if(matcher.find()){
            return false;
        }
        //不能重复
        QueryWrapper<Inbound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inbound_num", request.getInboundNum());
        long count = inboundMapper.selectCount(queryWrapper);
        if(count > 0){
            return false;
        }
        // 创建 Inbound 对象
        Inbound inbound = new Inbound();
        inbound.setInboundNum(request.getInboundNum());
        inbound.setSupplier(request.getSupplier());
        inbound.setInboundStatus(2); // 默认状态未入库
        inbound.setCreatedBy("admin"); // 假设创建人为 admin
        inbound.setCreatedTime(new Date());
        inboundMapper.insert(inbound);

        // 创建 InboundDetail 对象
        if(request.getInboundDetail() != null && !request.getInboundDetail().isEmpty()){
            for (InboundCreateRequest.InboundDetailRequest detailRequest : request.getInboundDetail()) {
                InboundDetail detail = getInboundDetail(request, detailRequest);
                inboundDetailMapper.insert(detail);
            }
        }
        return true;
    }

    @Override
    public List<Inbound> findInbound(String inboundStatus, String inboundNum, String supplier) {
        QueryWrapper<Inbound> queryWrapper = new QueryWrapper<>();

        if (inboundStatus != null && !inboundStatus.isEmpty()) {
            queryWrapper.eq("inbound_status", inboundStatus);
        }

        if (inboundNum != null && !inboundNum.isEmpty()) {
            queryWrapper.eq("inbound_num", inboundNum);
        }

        if (supplier != null && !supplier.isEmpty()) {
            queryWrapper.eq("supplier", supplier);
        }

        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteInbound(Integer id) {
        // Fetch the inbound entry by ID
        Inbound inbound = this.getById(id);
        if (inbound != null) {
            // Delete the inbound details associated with the inbound entry
            QueryWrapper<InboundDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inbound_num", inbound.getInboundNum());
            if (!queryWrapper.isEmptyOfWhere()) {
                inboundDetailMapper.delete(queryWrapper);
            }
            // Delete the inbound entry itself
            return this.removeById(id);
        }
        return false;
    }

    private static InboundDetail getInboundDetail(InboundCreateRequest request, InboundCreateRequest.InboundDetailRequest detailRequest) {
        InboundDetail detail = new InboundDetail();
        detail.setInboundNum(request.getInboundNum());
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




