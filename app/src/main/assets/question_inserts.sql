-- 西语专业词汇题目SQL（软件测试技术课程）
-- 说明：1-98题为选择题（西语选中文），99-196题为填空题（中文填西语）
-- 难度：1-32题难度1，33-64题难度2，65-98题难度3，填空题难度对应选择题

-- 4. 批量插入选择题（西语词汇选中文翻译）
INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (1, '¿Cuál es la traducción china correcta de "Mantenimiento"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (1, 1, '行覆盖率', '重构', '缺失的', '维护', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (2, '¿Cuál es la traducción china correcta de "Pruebas"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (2, 2, '调用、呼叫', '基本路径测试', '测试', '功能需求', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (3, '¿Cuál es la traducción china correcta de "Pruebas Unitarias"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (3, 3, '策略', '单元测试', '变量', '验证、校验', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (4, '¿Cuál es la traducción china correcta de "Cobertura Lógica"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (4, 4, '逻辑覆盖', '圈复杂度', '速度', '总覆盖率', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (5, '¿Cuál es la traducción china correcta de "Caja Negra"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (5, 5, '注解', '静态分析', '黑盒测试', '返回', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (6, '¿Cuál es la traducción china correcta de "Caja Blanca"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (6, 6, '组件', '应用', '白盒测试', '有效性', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (7, '¿Cuál es la traducción china correcta de "Assert"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (7, 7, '断言、验证', '维护', '抛出（异常等）', '循环', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (8, '¿Cuál es la traducción china correcta de "Decision"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (8, 8, '重复、已存在', '变量', '环境', '决策、分支', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (9, '¿Cuál es la traducción china correcta de "requisitos funcionales"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (9, 9, '路径覆盖', '速度', '功能需求', '循环', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (10, '¿Cuál es la traducción china correcta de "Validar"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (10, 10, '验证、确认', '报告', '框架', '可靠性', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (11, '¿Cuál es la traducción china correcta de "bucles"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (11, 11, '循环', '可视化', '环路复杂度', '状态', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (12, '¿Cuál es la traducción china correcta de "condiciones"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (12, 12, '基本路径测试', '条件', '覆盖率报告', '安装', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (13, '¿Cuál es la traducción china correcta de "Estrategia"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (13, 13, '存根、桩', '定量的', '隔离性', '策略', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (14, '¿Cuál es la traducción china correcta de "End-to-End"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (14, 14, '重复、已存在', '速度', '生命周期', '端到端', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (15, '¿Cuál es la traducción china correcta de "Pruebas de Integración"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (15, 15, '分支覆盖率', '维护', '集成测试', '流程图', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (16, '¿Cuál es la traducción china correcta de "Pruebas de Servicio"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (16, 16, '抛出（异常等）', '可维护性', '输入', '服务测试', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (17, '¿Cuál es la traducción china correcta de "Automatizada"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (17, 17, '隔离的', '分支覆盖率', '自动化', '创建', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (18, '¿Cuál es la traducción china correcta de "Aislada"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (18, 18, '衡量、测量', '隔离的', '测试替身', '标准库', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (19, '¿Cuál es la traducción china correcta de "variables"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (19, 19, '单元测试', '变量', '测试', '操作', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (20, '¿Cuál es la traducción china correcta de "Ejecutar"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (20, 20, '凭证', '执行', '资源', '定性的', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (21, '¿Cuál es la traducción china correcta de "Verificar"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (21, 21, '验证、校验', '密码', '变量', '终端', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (22, '¿Cuál es la traducción china correcta de "inválida"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (22, 22, '空着的', '流程图', '环境', '无效的', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (23, '¿Cuál es la traducción china correcta de "contrasena"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (23, 23, '生命周期', '密码', '服务测试', '可维护性', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (24, '¿Cuál es la traducción china correcta de "Herramientas"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (24, 24, '静态分析', '返回', '工具', '交互', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (25, '¿Cuál es la traducción china correcta de "Framework"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (25, 25, '依赖', '资源', '终端', '框架', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (26, '¿Cuál es la traducción china correcta de "anotación"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (26, 26, '资源', '模拟', '自动化', '注解', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (27, '¿Cuál es la traducción china correcta de "Métodos de aserción"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (27, 27, '重复、已存在', '断言方法', '异常', '创建', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (28, '¿Cuál es la traducción china correcta de "ciclo de vida"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (28, 28, '生命周期', '决策、分支', '循环', '注解', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (29, '¿Cuál es la traducción china correcta de "Utilidades"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (29, 29, '自动化', '返回', '变量', '实用程序、实用工具', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (30, '¿Cuál es la traducción china correcta de "patrón"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (30, 30, '速度', '模式', '认证', '路径覆盖', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (31, '¿Cuál es la traducción china correcta de "cobertura"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (31, 31, '分支覆盖率', '空着的', '集成测试', '覆盖率', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (32, '¿Cuál es la traducción china correcta de "Detectar"?', 'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (32, 32, '检测，识别', '框架', '策略', '行为', 'A');

-- 难度2选择题（33-64题）
INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (33, '¿Cuál es la traducción china correcta de "devuelve"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (33, 33, '创建', '重构', '安装', '返回', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (34, '¿Cuál es la traducción china correcta de "Efectividad"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (34, 34, '操作', '有效性', '认证', '覆盖率', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (35, '¿Cuál es la traducción china correcta de "Métrica"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (35, 35, '调用、呼叫', '指标、度量', '交互', '可靠性', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (36, '¿Cuál es la traducción china correcta de "mide"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (36, 36, '服务测试', '持续集成', '执行', '衡量、测量', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (37, '¿Cuál es la traducción china correcta de "Visual"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (37, 37, '验证、校验', '工具', '可视化', '输入', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (38, '¿Cuál es la traducción china correcta de "Informe"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (38, 38, '检测，识别', '单元测试', '工具', '报告', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (39, '¿Cuál es la traducción china correcta de "Informe de Cobertura"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (39, 39, '覆盖率报告', '重复、已存在', '密码', '单元测试', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (40, '¿Cuál es la traducción china correcta de "Cobertura Total"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (40, 40, '总覆盖率', '流程图', '文件系统', '策略', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (41, '¿Cuál es la traducción china correcta de "Cobertura de Ramas"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (41, 41, '认证', '框架', '分支覆盖率', '维护', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (42, '¿Cuál es la traducción china correcta de "Cobertura de Líneas"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (42, 42, '行覆盖率', '模式', '环境', '实用程序、实用工具', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (43, '¿Cuál es la traducción china correcta de "falta"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (43, 43, '可维护性', '实用程序、实用工具', '缺失的', '模拟', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (44, '¿Cuál es la traducción china correcta de "Expresión regular"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (44, 44, '正则表达式', '响应', '交互', '创建', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (45, '¿Cuál es la traducción china correcta de "entradas"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (45, 45, '检测，识别', '输入', '自动化', '返回', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (46, '¿Cuál es la traducción china correcta de "Configurar"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (46, 46, '重复、已存在', '仓库', '配置', '生命周期', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (47, '¿Cuál es la traducción china correcta de "Entorno"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (47, 47, '分支覆盖率', '环境', '决策、分支', '配置', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (48, '¿Cuál es la traducción china correcta de "Instalar"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (48, 48, '存根、桩', '安装', '指标、度量', '定性的', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (49, '¿Cuál es la traducción china correcta de "Aplicar"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (49, 49, '应用', '循环', '资源', '圈复杂度', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (50, '¿Cuál es la traducción china correcta de "Sistema de Archivos"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (50, 50, '框架', '文件系统', '集成测试', '调用', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (51, '¿Cuál es la traducción china correcta de "Servicio Correo"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (51, 51, '邮件服务', '生命周期', '响应', '无效的', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (52, '¿Cuál es la traducción china correcta de "Servicio Usuario"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (52, 52, '用户服务', '自动化', '覆盖率', '仓库', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (53, '¿Cuál es la traducción china correcta de "Dependencias"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (53, 53, '依赖', '总覆盖率', '缺失的', '可靠性', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (54, '¿Cuál es la traducción china correcta de "recursos"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (54, 54, '测试替身', '单元测试', '决策、分支', '资源', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (55, '¿Cuál es la traducción china correcta de "Dobles de Prueba"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (55, 55, '操作', '衡量、测量', '测试替身', '总覆盖率', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (56, '¿Cuál es la traducción china correcta de "Mock"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (56, 56, '存根、桩', '重构', '模拟', '覆盖率', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (57, '¿Cuál es la traducción china correcta de "Stub"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (57, 57, '衡量、测量', '环境', '用户服务', '存根、桩', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (58, '¿Cuál es la traducción china correcta de "Credenciales"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (58, 58, '密码', '可视化', '抛出（异常等）', '凭证', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (59, '¿Cuál es la traducción china correcta de "duplicado"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (59, 59, '交互', '验证、校验', '重复、已存在', '框架', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (60, '¿Cuál es la traducción china correcta de "permisos"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (60, 60, '操作', '权限', '终端', '文件系统', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (61, '¿Cuál es la traducción china correcta de "autenticación"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (61, 61, '断言方法', '返回', '认证', '邮件服务', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (62, '¿Cuál es la traducción china correcta de "CI"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (62, 62, '基本路径测试', '环路复杂度', '持续集成', '返回', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (63, '¿Cuál es la traducción china correcta de "CD"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (63, 63, '持续部署', '白盒测试', '创建', '行覆盖率', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (64, '¿Cuál es la traducción china correcta de "Velocidad"?', 'MULTIPLE_CHOICE', 2, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (64, 64, '速度', '圈复杂度', '自动化', '组件', 'A');

-- 难度3选择题（65-98题）
INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (65, '¿Cuál es la traducción china correcta de "Confiabilidad"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (65, 65, '行为', '空的、没有的', '可靠性', '测试金字塔', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (66, '¿Cuál es la traducción china correcta de "Mantenibilidad"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (66, 66, '维护', '可维护性', '权限', '总覆盖率', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (67, '¿Cuál es la traducción china correcta de "llamadas"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (67, 67, '凭证', '资源', '调用、呼叫', '权限', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (68, '¿Cuál es la traducción china correcta de "invocar"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (68, 68, '调用', '定性的', '集成测试', '衡量、测量', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (69, '¿Cuál es la traducción china correcta de "predefinidas"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (69, 69, '创建', '功能需求', '预定义', '副作用', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (70, '¿Cuál es la traducción china correcta de "respuestas"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (70, 70, '响应', '状态', '循环', '空的、没有的', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (71, '¿Cuál es la traducción china correcta de "comportamiento"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (71, 71, '端到端', '工具', '行为', '决策、分支', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (72, '¿Cuál es la traducción china correcta de "estado"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (72, 72, '状态', '增量、差值', '环境', '返回', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (73, '¿Cuál es la traducción china correcta de "interacciones"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (73, 73, '交互', '隔离性', '调用、呼叫', '基本路径测试', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (74, '¿Cuál es la traducción china correcta de "retorno"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (74, 74, '返回', '测试金字塔', '可视化', '报告', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (75, '¿Cuál es la traducción china correcta de "Diagrama de flujo"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (75, 75, '环境', '测试金字塔', '异常', '流程图', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (76, '¿Cuál es la traducción china correcta de "acciones"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (76, 76, '分支覆盖率', '副作用', '功能需求', '操作', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (77, '¿Cuál es la traducción china correcta de "Librería estándar"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (77, 77, '测试', '工具', '可维护性', '标准库', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (78, '¿Cuál es la traducción china correcta de "excepción"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (78, 78, '异常', '文件系统', '仓库', '工具', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (79, '¿Cuál es la traducción china correcta de "lanza"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (79, 79, '输入', '抛出（异常等）', '行为', '验证、确认', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (80, '¿Cuál es la traducción china correcta de "Creación"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (80, 80, '自动化', '覆盖率', '空着的', '创建', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (81, '¿Cuál es la traducción china correcta de "Componente"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (81, 81, '组件', '条件', '注解', '副作用', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (82, '¿Cuál es la traducción china correcta de "Cobertura de Ruta"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (82, 82, '隔离性', '单元测试', '路径覆盖', '终端', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (83, '¿Cuál es la traducción china correcta de "Efectos laterales"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (83, 83, '状态', '检测，识别', '覆盖率报告', '副作用', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (84, '¿Cuál es la traducción china correcta de "Complejidad exponencial"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (84, 84, '隔离的', '功能需求', '指数级复杂性', '缺失的', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (85, '¿Cuál es la traducción china correcta de "Pruebas de Ruta Básica"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (85, 85, '交互', '验证、校验', '基本路径测试', '决策、分支', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (86, '¿Cuál es la traducción china correcta de "Complejidad Ciclomática"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (86, 86, '环路复杂度', '邮件服务', '权限', '可靠性', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (87, '¿Cuál es la traducción china correcta de "refactorizar"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (87, 87, '断言方法', '重构', '存根、桩', '速度', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (88, '¿Cuál es la traducción china correcta de "cuantitativa"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (88, 88, '框架', '定量的', '返回', '隔离的', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (89, '¿Cuál es la traducción china correcta de "cualitativa"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (89, 89, '断言方法', '定性的', '空的、没有的', '抛出（异常等）', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (90, '¿Cuál es la traducción china correcta de "repositorio"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (90, 90, '调用、呼叫', '凭证', '仓库', '维护', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (91, '¿Cuál es la traducción china correcta de "terminal"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (91, 91, '模式', '终端', '验证、校验', '分支覆盖率', 'B');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (92, '¿Cuál es la traducción china correcta de "delta"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (92, 92, '重构', '隔离的', '增量、差值', '逻辑覆盖', 'C');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (93, '¿Cuál es la traducción china correcta de "nulo"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (93, 93, '返回', '副作用', '有效性', '空的、没有的', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (94, '¿Cuál es la traducción china correcta de "vacío"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (94, 94, '空着的', '断言、验证', '可维护性', '创建', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (95, '¿Cuál es la traducción china correcta de "Análisis Estático"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (95, 95, '凭证', '缺失的', '衡量、测量', '静态分析', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (96, '¿Cuál es la traducción china correcta de "Complejidad Ciclomática"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (96, 96, '圈复杂度', '路径覆盖', '总覆盖率', '返回', 'A');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (97, '¿Cuál es la traducción china correcta de "Pirámide de Pruebas"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (97, 97, '自动化', '仓库', '无效的', '测试金字塔', 'D');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (98, '¿Cuál es la traducción china correcta de "Aislamiento"?', 'MULTIPLE_CHOICE', 3, 'SOFTWARE_TEST');
INSERT INTO choice_question (choice_question_id, question_id, option_a, option_b, option_c, option_d, correct_option)
VALUES (98, 98, '增量、差值', '隔离性', '文件系统', '可靠性', 'B');

-- 填空题（99-196题，难度对应选择题）
INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (99, 'Completa la palabra española: La traducción de "维护" es _____.', 'FILL_BLANK', 1, 'SOFTWARE_TEST');
INSERT INTO fill_blank_question (fill_question_id, question_id, complete_sentence, sentence_with_blank, correct_answer, hint)
VALUES (1, 99, 'La traducción de "维护" es Mantenimiento.', 'La traducción de "维护" es _____.', 'Mantenimiento', 'Palabra española relacionada con 维护');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (100, 'Completa la palabra española: La traducción de "测试" es _____.', 'FILL_BLANK', 1, 'SOFTWARE_TEST');
INSERT INTO fill_blank_question (fill_question_id, question_id, complete_sentence, sentence_with_blank, correct_answer, hint)
VALUES (2, 100, 'La traducción de "测试" es Pruebas.', 'La traducción de "测试" es _____.', 'Pruebas', 'Palabra española relacionada con 测试');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (101, 'Completa la palabra española: La traducción de "单元测试" es _____.', 'FILL_BLANK', 1, 'SOFTWARE_TEST');
INSERT INTO fill_blank_question (fill_question_id, question_id, complete_sentence, sentence_with_blank, correct_answer, hint)
VALUES (3, 101, 'La traducción de "单元测试" es Pruebas Unitarias.', 'La traducción de "单元测试" es _____.', 'Pruebas Unitarias', 'Palabra española relacionada con 单元测试');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (102, 'Completa la palabra española: La traducción de "逻辑覆盖" es _____.', 'FILL_BLANK', 1, 'SOFTWARE_TEST');
INSERT INTO fill_blank_question (fill_question_id, question_id, complete_sentence, sentence_with_blank, correct_answer, hint)
VALUES (4, 102, 'La traducción de "逻辑覆盖" es Cobertura Lógica.', 'La traducción de "逻辑覆盖" es _____.', 'Cobertura Lógica', 'Palabra española relacionada con 逻辑覆盖');

INSERT INTO spanish_question (question_id, content, type, difficulty, category)
VALUES (103, 'Completa la palabra española: La traducción de "黑盒测试" es _____.', 'FILL_BLANK', 1, 'SOFTWARE_TEST');
INSERT