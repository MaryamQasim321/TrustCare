package com.example.trustcare.repository;

import com.example.trustcare.model.SnsSubscription;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Repository
public class SubscriptionDAO {
    private final JdbcTemplate jdbcTemplate;
    public SubscriptionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_SUBSCRIPTION_BY_USER_ID="SELECT user_id, email, subscription_arn, last_used_date, active FROM sns_subscriptions WHERE user_id=?";
    private static final String SAVE_USER= "INSERT INTO sns_subscriptions(user_id, email, subscription_arn, last_used_date, active) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE email = VALUES(email), subscription_arn = VALUES(subscription_arn), last_used_date = VALUES(last_used_date), active = VALUES(active)";
    private static  final String DEACTIVATE="Update sns_subscriptions set active=false where user_id=?";
    private static final String UPDATE_LAST_USED="UPDATE sns_subscriptions set last_used_date=? where user_id=?";
    private static final String FIND_OLDER_THAN="SELECT user_id, email, subscription_arn, last_used_date, active FROM sns_subscriptions WHERE active = true AND last_used_date < ?";
    public SnsSubscription getSubscriptionById(int userId) {
        return jdbcTemplate.queryForObject(

                GET_SUBSCRIPTION_BY_USER_ID, (rs, rowNum) -> {
                    SnsSubscription s = new SnsSubscription();
                    s.setUserId(rs.getInt("user_id"));
                    s.setEmail(rs.getString("email"));
                    s.setSubscriptionArn(rs.getString("subscription_arn"));
                    java.sql.Date d = rs.getDate("last_used_date");
                    if (d != null) s.setLastUsedDate(d.toLocalDate());
                    s.setActive(rs.getBoolean("active"));
                    return s;
                }, userId
        );

    }
        public void save(SnsSubscription subscription){
        jdbcTemplate.update(SAVE_USER,
                subscription.getUserId(), subscription.getEmail(), subscription.getSubscriptionArn(), subscription.getLastUsedDate(), subscription.getLastUsedDate(), subscription.isActive());
        }

        public void deactivate(int userId){
        jdbcTemplate.update(DEACTIVATE, userId);

        }
        public void lastUsed(int userId){
        jdbcTemplate.update(UPDATE_LAST_USED, LocalDate.now(), userId);

        }

    public List<SnsSubscription> findOlderThan(LocalDate localDate) {
        return jdbcTemplate.query(
                FIND_OLDER_THAN,
                (rs, rowNum) -> {
                    SnsSubscription s = new SnsSubscription();
                    s.setUserId(rs.getInt("user_id"));
                    s.setEmail(rs.getString("email"));
                    s.setSubscriptionArn(rs.getString("subscription_arn"));
                    java.sql.Date d = rs.getDate("last_used_date");
                    if (d != null) s.setLastUsedDate(d.toLocalDate());
                    s.setActive(rs.getBoolean("active"));
                    return s;
                },
                localDate
        );
    }










}
