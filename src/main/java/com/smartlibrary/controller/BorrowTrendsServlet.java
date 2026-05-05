package com.smartlibrary.controller;

import com.smartlibrary.util.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BorrowTrendsServlet extends HttpServlet {

    private static final class TrendRow {
        private final String label;
        private final int borrows;
        private final int returns;

        private TrendRow(String label, int borrows, int returns) {
            this.label = label;
            this.borrows = borrows;
            this.returns = returns;
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (userId == null || role == null || !"admin".equals(role)) {
            out.print("{\"success\":false}");
            return;
        }
        
        String period = request.getParameter("period");
        if (period == null) period = "thisMonth";
        
        try {
            Connection conn = DBConnection.getConnection();

            TrendRow[] rows;
            if ("lastMonth".equals(period)) {
                rows = getWeeklyTrends(conn, false);
            } else if ("last3Months".equals(period)) {
                rows = getMonthlyTrends(conn);
            } else {
                rows = getWeeklyTrends(conn, true);
            }
            
            out.print("[");
            for (int i = 0; i < rows.length; i++) {
                if (i > 0) out.print(",");
                TrendRow row = rows[i];
                out.print("{\"label\":\"" + escapeJson(row.label) +
                    "\",\"borrows\":" + row.borrows +
                    ",\"returns\":" + row.returns + "}");
            }
            out.print("]");

            conn.close();

        } catch (Exception e) {
            out.print("[]");
        }
    }

    private TrendRow[] getWeeklyTrends(Connection conn, boolean currentMonth) throws Exception {
        String sql =
            "WITH bounds AS (" +
            "    SELECT DATE_TRUNC('month', CURRENT_TIMESTAMP" +
            (currentMonth ? "" : " - INTERVAL '1 month'") +
            ") AS start_date" +
            "), weeks AS (" +
            "    SELECT 1 AS week_no, start_date AS week_start, LEAST(start_date + INTERVAL '7 days', start_date + INTERVAL '1 month') AS week_end FROM bounds " +
            "    UNION ALL " +
            "    SELECT 2 AS week_no, start_date + INTERVAL '7 days', LEAST(start_date + INTERVAL '14 days', start_date + INTERVAL '1 month') FROM bounds " +
            "    UNION ALL " +
            "    SELECT 3 AS week_no, start_date + INTERVAL '14 days', LEAST(start_date + INTERVAL '21 days', start_date + INTERVAL '1 month') FROM bounds " +
            "    UNION ALL " +
            "    SELECT 4 AS week_no, start_date + INTERVAL '21 days', start_date + INTERVAL '1 month' FROM bounds" +
            ") " +
            "SELECT " +
            "    week_no, " +
            "    COALESCE((SELECT COUNT(*) FROM borrows b WHERE b.borrow_date >= week_start AND b.borrow_date < week_end), 0) AS borrows, " +
            "    COALESCE((SELECT COUNT(*) FROM borrows b WHERE b.return_date >= week_start AND b.return_date < week_end), 0) AS returns " +
            "FROM weeks " +
            "WHERE week_start < week_end " +
            "ORDER BY week_no";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        TrendRow[] rows = new TrendRow[4];
        int index = 0;
        while (rs.next()) {
            rows[index++] = new TrendRow(
                "Week " + rs.getInt("week_no"),
                rs.getInt("borrows"),
                rs.getInt("returns")
            );
        }

        rs.close();
        stmt.close();
        return rows;
    }

    private TrendRow[] getMonthlyTrends(Connection conn) throws Exception {
        String sql =
            "WITH months AS (" +
            "    SELECT GENERATE_SERIES(" +
            "        DATE_TRUNC('month', CURRENT_TIMESTAMP) - INTERVAL '2 months', " +
            "        DATE_TRUNC('month', CURRENT_TIMESTAMP), " +
            "        INTERVAL '1 month'" +
            "    ) AS month_start" +
            ") " +
            "SELECT " +
            "    TO_CHAR(month_start, 'Mon') AS label, " +
            "    COALESCE((SELECT COUNT(*) FROM borrows b WHERE DATE_TRUNC('month', b.borrow_date) = month_start), 0) AS borrows, " +
            "    COALESCE((SELECT COUNT(*) FROM borrows b WHERE b.return_date IS NOT NULL AND DATE_TRUNC('month', b.return_date) = month_start), 0) AS returns " +
            "FROM months " +
            "ORDER BY month_start";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        TrendRow[] rows = new TrendRow[3];
        int index = 0;
        while (rs.next()) {
            rows[index++] = new TrendRow(
                rs.getString("label").trim(),
                rs.getInt("borrows"),
                rs.getInt("returns")
            );
        }

        rs.close();
        stmt.close();
        return rows;
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"");
    }
}
