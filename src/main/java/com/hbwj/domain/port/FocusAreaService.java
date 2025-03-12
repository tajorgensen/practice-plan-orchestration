package com.hbwj.domain.port;

import com.hbwj.domain.model.FocusArea;

import java.util.List;

public interface FocusAreaService {
    List<FocusArea> getAllFocusAreas();

    FocusArea getFocusAreaById(Long id);

    FocusArea createFocusArea(FocusArea focusArea);

    FocusArea updateFocusArea(Long id, FocusArea focusArea);

    void deleteFocusArea(Long id);
}
