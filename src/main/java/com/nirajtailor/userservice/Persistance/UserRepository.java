package com.nirajtailor.userservice.Persistance;

import com.nirajtailor.userservice.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    @Qualifier("firstJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondJdbcTemplate")
    private JdbcTemplate secondtJdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<User> getUsers(Long userId, String name){
        String sql = "select * from user where 1 = 1 ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (userId != null) {
            sql += "and id = :id ";
            parameters.addValue("id" , userId);
        }
        if (StringUtils.hasText(name)) {
            sql += "and name = :name ";
            parameters.addValue("name" , name);
        }
        List<User> Users = namedParameterJdbcTemplate.query(sql, parameters, new UserMapper());
//        List<User> Users = jdbcTemplate.query(sql, new UserMapper());
        if (CollectionUtils.isEmpty(Users)) {
            return null;
        }
        return Users;
    }

    public User getUserById(Long userId) {
        String sql = "select * from user where id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id" , userId);
        List<User> Users = namedParameterJdbcTemplate.query(sql, parameters, new UserMapper());
        if (CollectionUtils.isEmpty(Users)) {
            return null;
        }
        return Users.get(0);
    }

    public User createUser1(User user) {
        String sql = "INSERT INTO user (name, email, mobile) VALUES (?, ?, ?)";
        int r = jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getMobile());
        return user;
    }

    /*

    public int createUsers(List<User> userList) {
        String sql = "INSERT INTO user (name, email, mobile) VALUES (?, ?, ?)";

        int[] row = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, userList.get(i).getName());
                ps.setString(2, userList.get(i).getEmail());
                ps.setString(3, userList.get(i).getMobile());
            }
            @Override
            public int getBatchSize() {
                return userList.size();
            }
        });
        return Arrays.stream(row).sum();
    }

     */

    public int createUsers(List<User> userList) {
        String sql = "INSERT INTO user (name, email, mobile) VALUES (?, ?, ?)";
        // batchsize <= list.size();
        int[][] row = jdbcTemplate.batchUpdate(sql, userList, 2, new ParameterizedPreparedStatementSetter<User>() {
            @Override
            public void setValues(PreparedStatement ps, User user) throws SQLException {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getMobile());
            }
        });
        // n*batchsize where n = list.size()/batchsize
        return row.length;
    }

    public User createUser(User user) {
        String sql = "INSERT INTO user (name, email, mobile) VALUES (:name, :email, :mobile)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", user.getName());
        parameters.addValue("email", user.getEmail());
        parameters.addValue("mobile", user.getMobile());
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        int r = namedParameterJdbcTemplate.update(sql, parameters, generatedKeyHolder);

        if(!CollectionUtils.isEmpty(generatedKeyHolder.getKeys())){
            Long userId = Long.parseLong(String.valueOf(generatedKeyHolder.getKeys().get("GENERATED_KEY")));
            user.setId(userId);
        }
        return user;
    }

    public User updateUser(Long id, User user) {
        String sql = "UPDATE user SET name = :name "
                + " where id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        parameters.addValue("name", user.getName());
        int r = namedParameterJdbcTemplate.update(sql, parameters);
        user.setId(id);
        return user;
    }

    private final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            return User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .mobile(rs.getString("mobile"))
                    .build();
        }
    }
}
