package com.backend.liaison_system.region.dao;

import java.util.List;
import java.util.Map;

public record Regions(
        Map<String, List<String>> regions
) { }
