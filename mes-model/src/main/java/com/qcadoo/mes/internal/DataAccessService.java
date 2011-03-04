/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo MES
 * Version: 0.3.0
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */

package com.qcadoo.mes.internal;

import java.util.Set;

import com.qcadoo.mes.model.search.SearchCriteria;
import com.qcadoo.mes.model.search.SearchResult;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.internal.api.InternalDataDefinition;

public interface DataAccessService {

    Entity save(InternalDataDefinition dataDefinition, Entity entity);

    Entity get(InternalDataDefinition dataDefinition, Long entityId);

    Entity copy(InternalDataDefinition dataDefinition, Long entityId);

    Set<Long> copy(InternalDataDefinition dataDefinition, Set<Long> entityIds);

    void delete(InternalDataDefinition dataDefinition, Long... entityId);

    SearchResult find(SearchCriteria searchCriteria);

    void moveTo(InternalDataDefinition dataDefinition, Long entityId, int position);

    void move(InternalDataDefinition dataDefinition, Long entityId, int offset);
}
