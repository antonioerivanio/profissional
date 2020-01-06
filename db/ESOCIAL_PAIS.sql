UPDATE srh.esocial_pais
SET
	dataextincao = to_date('30/04/2018')
WHERE
	codigo IN (
		'047',
		'151',
		'452',
		'396',
		'423',
		'490',
		'873'
	);
/**/
UPDATE srh.esocial_pais
SET
	dataextincao = to_date('27/06/2016')
WHERE
	codigo IN (
		'678'
	);
/**/
UPDATE srh.esocial_pais
SET
	dataextincao = to_date('21/11/2013')
WHERE
	codigo IN (
		'388'
	);
/**/
UPDATE srh.esocial_pais
SET
	descricao = 'Jersey',
	codigo = '393'
WHERE
	codigo = '150';
/**/
UPDATE srh.esocial_pais
SET
	descricao = 'Papua Nova Guine'
WHERE
	codigo = '583';
/***/
DELETE FROM srh.esocial_pais
WHERE
	id = 222;
/**/
INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'015',
	'Aland, Ilhas',
	to_date('07/12/2015'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'042',
	'Antartica',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'099',
	'Bonaire, Saint Eustatius E Saba',
	to_date('07/12/2015'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'102',
	'Bouvet, Ilha',
	to_date('07/12/2015'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'200',
	'Curacao',
	to_date('07/12/2015'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'754',
	'eSwatini',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'292',
	'Georgia Do Sul E Sandwich Do Sul, Ilhas',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'321',
	'Guernsey',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'343',
	'Heard E Ilhas McDonald, Ilha',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'150',
	'Ilhas Do Canal (Jersey E Guernsey)',
	NULL,
	to_date('30/04/2018')
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'489',
	'Mayotte',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'498',
	'Montenegro',
	to_date('03/06/2006'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'693',
	'Sao Bartolomeu',
	to_date('07/12/2015'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'698',
	'Sao Martinho, Ilha De (Parte Francesa)',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'699',
	'Sao Martinho, Ilha De (Parte Holandesa)',
	to_date('07/12/2015'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'737',
	'Servia',
	to_date('03/06/2006'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'760',
	'Sudao Do Sul',
	to_date('27/05/2013'),
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'755',
	'Svalbard E Jan Mayen',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'781',
	'Terras Austrais Francesas',
	NULL,
	NULL
);

INSERT INTO srh.esocial_pais (
	id,
	codigo,
	descricao,
	datacriacao,
	dataextincao
) VALUES (
	(
		SELECT
			MAX(id) + 1
		FROM
			srh.esocial_pais
	),
	'895',
	'Zona Do Canal Do Panama',
	NULL,
	to_date('27/06/2016')
);


