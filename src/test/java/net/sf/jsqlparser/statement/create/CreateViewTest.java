/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package net.sf.jsqlparser.statement.create;

import junit.framework.TestCase;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.io.StringReader;

import static net.sf.jsqlparser.test.TestUtils.assertSqlCanBeParsedAndDeparsed;

public class CreateViewTest extends TestCase {

    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    public CreateViewTest(String arg0) {
        super(arg0);
    }

    public void testCreateView() throws JSQLParserException {
        String statement = "CREATE VIEW myview AS SELECT * FROM mytab";
        CreateView createView = (CreateView) parserManager.parse(new StringReader(statement));
        assertFalse(createView.isOrReplace());
        assertEquals("myview", createView.getView().getName());
        assertEquals("mytab", ((Table) ((PlainSelect) createView.getSelect().getSelectBody()).getFromItem()).getName());
        assertEquals(statement, createView.toString());
    }

    public void testCreateView2() throws JSQLParserException {
        String stmt = "CREATE VIEW myview AS SELECT * FROM mytab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateView3() throws JSQLParserException {
        String stmt = "CREATE OR REPLACE VIEW myview AS SELECT * FROM mytab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateView4() throws JSQLParserException {
        String stmt = "CREATE OR REPLACE VIEW view2 AS SELECT a, b, c FROM testtab INNER JOIN testtab2 ON testtab.col1 = testtab2.col2";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateViewWithColumnNames1() throws JSQLParserException {
        String stmt = "CREATE OR REPLACE VIEW view1(col1, col2) AS SELECT a, b FROM testtab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateView5() throws JSQLParserException {
        String statement = "CREATE VIEW myview AS (SELECT * FROM mytab)";
        String statement2 = "CREATE VIEW myview AS (SELECT * FROM mytab)";
        CreateView createView = (CreateView) parserManager.parse(new StringReader(statement));
        assertFalse(createView.isOrReplace());
        assertEquals("myview", createView.getView().getName());
        assertEquals("mytab", ((Table) ((PlainSelect) createView.getSelect().getSelectBody()).getFromItem()).
                getName());
        assertEquals(statement2, createView.toString());
    }

    public void testCreateViewUnion() throws JSQLParserException {
        String stmt = "CREATE VIEW view1 AS (SELECT a, b FROM testtab) UNION (SELECT b, c FROM testtab2)";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }

    public void testCreateMaterializedView() throws JSQLParserException {
        String stmt = "CREATE MATERIALIZED VIEW view1 AS SELECT a, b FROM testtab";
        assertSqlCanBeParsedAndDeparsed(stmt);
    }
    
    public void testCreateForceView() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE FORCE VIEW view1 AS SELECT a, b FROM testtab");
    }
    
    public void testCreateForceView1() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE NO FORCE VIEW view1 AS SELECT a, b FROM testtab");
    }
    
    public void testCreateForceView2() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE OR REPLACE FORCE VIEW view1 AS SELECT a, b FROM testtab");
    }
    
    public void testCreateForceView3() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE OR REPLACE NO FORCE VIEW view1 AS SELECT a, b FROM testtab");
    }
    
    public void testCreateTemporaryViewIssue604() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE TEMPORARY VIEW myview AS SELECT * FROM mytable");
    }
    
    public void testCreateTemporaryViewIssue604_2() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE TEMP VIEW myview AS SELECT * FROM mytable");
    }

    public void testCreateTemporaryViewIssue665() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE VIEW foo(\"BAR\") AS WITH temp AS (SELECT temp_bar FROM foobar) SELECT bar FROM temp");
    }

    public void testOrReplaceViewWithWithStatement() throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed("CREATE OR REPLACE VIEW foo AS (WITH t as (Select 1 from dual) Select * from t) ");
    }
}
