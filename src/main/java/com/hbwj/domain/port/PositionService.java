package com.hbwj.domain.port;

import com.hbwj.domain.model.Position;

import java.util.List;

public interface PositionService {
    List<Position> getAllPositions();

    List<Position> getPositionsBySportId(Long sportId);

    Position getPositionById(Long id);

    Position createPosition(Position position);

    Position updatePosition(Long id, Position position);

    void deletePosition(Long id);

    List<Position> getPositionsBySportIdAndPositionType(Long sportId, String positionType);

    List<Position> getPositionsBySportIdAndPositionTypeIncludingBoth(Long sportId, String positionType);
}
