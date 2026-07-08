package com.crm.dto;

import java.util.Map;

public class DashboardStats {
    private long totalLeads;
    private long todayFollowups;
    private long pendingFollowups;
    private long hotCustomers;
    private long closedWonDeals;
    private long closedLostDeals;
    private Map<String, Long> leadsByStatus;
    private Map<String, Long> leadsByPriority;
    private Map<String, Long> leadsBySector;

    public DashboardStats() {}

    // Getters and Setters
    public long getTotalLeads() { return totalLeads; }
    public void setTotalLeads(long totalLeads) { this.totalLeads = totalLeads; }

    public long getTodayFollowups() { return todayFollowups; }
    public void setTodayFollowups(long todayFollowups) { this.todayFollowups = todayFollowups; }

    public long getPendingFollowups() { return pendingFollowups; }
    public void setPendingFollowups(long pendingFollowups) { this.pendingFollowups = pendingFollowups; }

    public long getHotCustomers() { return hotCustomers; }
    public void setHotCustomers(long hotCustomers) { this.hotCustomers = hotCustomers; }

    public long getClosedWonDeals() { return closedWonDeals; }
    public void setClosedWonDeals(long closedWonDeals) { this.closedWonDeals = closedWonDeals; }

    public long getClosedLostDeals() { return closedLostDeals; }
    public void setClosedLostDeals(long closedLostDeals) { this.closedLostDeals = closedLostDeals; }

    public Map<String, Long> getLeadsByStatus() { return leadsByStatus; }
    public void setLeadsByStatus(Map<String, Long> leadsByStatus) { this.leadsByStatus = leadsByStatus; }

    public Map<String, Long> getLeadsByPriority() { return leadsByPriority; }
    public void setLeadsByPriority(Map<String, Long> leadsByPriority) { this.leadsByPriority = leadsByPriority; }

    public Map<String, Long> getLeadsBySector() { return leadsBySector; }
    public void setLeadsBySector(Map<String, Long> leadsBySector) { this.leadsBySector = leadsBySector; }
}
