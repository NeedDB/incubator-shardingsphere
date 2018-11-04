/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.parsing.antler.phrase.visitor;

import io.shardingsphere.core.parsing.antler.sql.ddl.AlterTableStatement;
import io.shardingsphere.core.parsing.antler.sql.ddl.ColumnDefinition;
import io.shardingsphere.core.parsing.antler.util.RuleNameConstants;
import io.shardingsphere.core.parsing.antler.util.TreeUtils;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * Visit add primary key phrase.
 * 
 * @author duhongjun
 */
@RequiredArgsConstructor
public final class AddPrimaryKeyVisitor implements PhraseVisitor {
    
    private final String ruleName;
    
    @Override
    public void visit(final ParserRuleContext ancestorNode, final SQLStatement statement) {
        AlterTableStatement alterStatement = (AlterTableStatement) statement;
        ParserRuleContext modifyColumnContext = TreeUtils.getFirstChildByRuleName(ancestorNode, ruleName);
        if (null == modifyColumnContext) {
            return;
        }
        ParserRuleContext primaryKeyContext = TreeUtils.getFirstChildByRuleName(modifyColumnContext, RuleNameConstants.PRIMARY_KEY);
        if (null == primaryKeyContext) {
            return;
        }
        List<ParserRuleContext> columnNodes = TreeUtils.getAllDescendantByRuleName(modifyColumnContext, RuleNameConstants.COLUMN_NAME);
        if (null == columnNodes) {
            return;
        }
        for (ParserRuleContext each : columnNodes) {
            String columnName = each.getText();
            ColumnDefinition updateColumn = alterStatement.getColumnDefinitionByName(columnName);
            if (null != updateColumn) {
                updateColumn.setPrimaryKey(true);
                alterStatement.getUpdateColumns().put(columnName, updateColumn);
            }
        }
    }
}