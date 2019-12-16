DECLARE
    codigo     VARCHAR2(2);
    invalido   NUMBER;
BEGIN
    FOR dep IN (
        SELECT
            *
        FROM
            tb_dependente
    ) LOOP
        CASE dep.idtipodependencia
            WHEN 0 THEN
                codigo := '99';
                invalido := 0;
            WHEN 1 THEN
                codigo := '01';
                invalido := 0;
            WHEN 2 THEN
                codigo := '02';
                invalido := 0;
            WHEN 3 THEN
                codigo := '03';
                invalido := 0;
            WHEN 4 THEN
                codigo := '03';
                invalido := 1;
            WHEN 5 THEN
                codigo := '09';
                invalido := 0;
            WHEN 6 THEN
                codigo := '06';
                invalido := 0;
            WHEN 7 THEN
                codigo := '06';
                invalido := 1;
            WHEN 8 THEN
                codigo := '03';
                invalido := 0;
            WHEN 9 THEN
                codigo := '03';
                invalido := 1;
            WHEN 10 THEN
                codigo := '11';
                invalido := 0;
            WHEN 11 THEN
                codigo := '11';
                invalido := 1;
            WHEN 12 THEN
                codigo := '04';
                invalido := 0;
            WHEN 13 THEN
                codigo := '03';
                invalido := 0;
        END CASE;

        UPDATE tb_dependente
        SET
            codigoesocial = codigo,
            flinvalido = invalido
        WHERE
            id = dep.id;

    END LOOP;
END;

COMMIT;