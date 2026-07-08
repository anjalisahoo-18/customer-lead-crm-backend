package com.crm.service;

import com.crm.dto.DashboardStats;
import com.crm.repository.CustomerLeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private CustomerLeadRepository leadRepository;

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        
        stats.setTotalLeads(leadRepository.count());
        stats.setTodayFollowups(leadRepository.countFollowupsForDate(LocalDate.now()));
        stats.setPendingFollowups(leadRepository.countOverdueFollowups(LocalDate.now()));
        stats.setHotCustomers(leadRepository.countByPriority("Hot"));
        stats.setClosedWonDeals(leadRepository.countByStatus("Closed Won"));
        stats.setClosedLostDeals(leadRepository.countByStatus("Closed Lost"));

        stats.setLeadsByStatus(convertToMap(leadRepository.getLeadsCountByStatus()));
        stats.setLeadsByPriority(convertToMap(leadRepository.getLeadsCountByPriority()));
        stats.setLeadsBySector(convertToMap(leadRepository.getLeadsCountByLeadType()));

        return stats;
    }

    private Map<String, Long> convertToMap(List<Object[]> results) {
        Map<String, Long> map = new HashMap<>();
        for (Object[] result : results) {
            String key = (String) result[0];
            Long count = (Long) result[1];
            if (key != null) {
                map.put(key, count);
            }
        }
        return map;
    }
}
