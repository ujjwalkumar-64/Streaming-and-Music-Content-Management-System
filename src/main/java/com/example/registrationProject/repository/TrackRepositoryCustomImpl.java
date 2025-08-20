package com.example.registrationProject.repository;

import com.example.registrationProject.response.DTO.TrackTrendDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrackRepositoryCustomImpl implements TrackRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TrackRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TrackTrendDto> findAllTrendingTrackIds() {
        String sql = """
            SELECT
                            t.id AS track_id,
                            COALESCE(l.count_like, 0) AS count_like,
                            COALESCE(s.count_stream, 0) AS count_stream,
                            (COALESCE(l.count_like, 0) * 2 + COALESCE(s.count_stream, 0)) AS calc_trend
            FROM
                track t
            LEFT JOIN (
                SELECT
                    track_id,
                    COUNT(*) AS count_like
                FROM
                    track_like
                WHERE 
                    status = 'active'
                GROUP BY 
                    track_id
            ) l ON t.id = l.track_id
            LEFT JOIN (
                SELECT 
                    track_id, 
                    COUNT(*) AS count_stream
                FROM 
                    track_stream_log
                WHERE 
                    status = 'active'
                GROUP BY 
                    track_id
            ) s ON t.id = s.track_id
            WHERE 
                t.status = 'active'
            ORDER BY 
                (COALESCE(l.count_like, 0) * 2 + COALESCE(s.count_stream, 0)) DESC
            LIMIT 5;
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new TrackTrendDto(
                rs.getLong("track_id"),
                rs.getInt("count_like"),
                rs.getInt("count_stream"),
                rs.getInt("calc_trend")
        ));
    }
}
