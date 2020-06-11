--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.14
-- Dumped by pg_dump version 9.6.14

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: certificate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.certificate (
    id integer NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(1024),
    price numeric,
    creation timestamp with time zone NOT NULL,
    modification timestamp with time zone,
    duration integer
);


ALTER TABLE public.certificate OWNER TO postgres;

--
-- Name: certificate_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.certificate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.certificate_id_seq OWNER TO postgres;

--
-- Name: certificate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.certificate_id_seq OWNED BY public.certificate.id;


--
-- Name: certificate_tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.certificate_tag (
    certificate_id integer NOT NULL,
    tag_id integer NOT NULL
);


ALTER TABLE public.certificate_tag OWNER TO postgres;

--
-- Name: tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tag (
    id integer NOT NULL,
    name character varying(128)
);


ALTER TABLE public.tag OWNER TO postgres;

--
-- Name: tag_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tag_id_seq OWNER TO postgres;

--
-- Name: tag_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tag_id_seq OWNED BY public.tag.id;


--
-- Name: certificate id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate ALTER COLUMN id SET DEFAULT nextval('public.certificate_id_seq'::regclass);


--
-- Name: tag id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag ALTER COLUMN id SET DEFAULT nextval('public.tag_id_seq'::regclass);


--
-- Data for Name: certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.certificate (id, name, description, price, creation, modification, duration) FROM stdin;
278	Dcertificate 3	description 3	99.15	2020-02-01 12:22:54+03	2020-06-11 13:47:16.514048+03	20
284	Ctwo	description two	99.15	2020-05-01 13:22:54+03	2020-06-11 13:47:16.653466+03	20
283	Ecertificate 3	description one	99.15	2020-04-01 16:22:54+03	2020-06-11 13:47:16.743055+03	20
279	Bcertificate 4	description 4	99.15	2020-07-01 15:22:54+03	2020-06-11 13:47:16.807964+03	20
280	Ecertificate 3	description upd	99.15	2020-03-01 16:22:54+03	2020-06-11 13:51:56.161491+03	20
315	cert a	description a	99.15	2020-06-11 13:55:52.225379+03	\N	20
316	cert b	description b	100	2020-06-11 13:56:32.920087+03	\N	20
317	cert ab	description ab	100	2020-06-11 13:56:56.408+03	\N	30
318	cert abc	description abc	100	2020-06-11 13:57:04.044289+03	\N	30
320	cert abcd	description abcd	100	2020-06-11 13:58:32.180181+03	\N	30
319	cert abcde	description abcde	100	2020-06-11 13:57:14.451412+03	2020-06-11 14:00:25.2129+03	30
321	cert abcdef	description abcdef	100	2020-06-11 14:00:43.304817+03	\N	30
322	cert abcdefg	description abcdefg	100	2020-06-11 14:00:50.012003+03	\N	30
323	cert abcdefgh	description abcdefgh	100	2020-06-11 14:01:08.937572+03	\N	30
324	cert abcdefgh	description abcdefgh	100	2020-06-11 14:01:43.773539+03	\N	30
325	cert abcdefghj	description abcdefghj	100	2020-06-11 14:01:56.060398+03	\N	30
326	cert q	description q	100	2020-06-11 14:02:18.553432+03	\N	30
327	cert qq	description qq	100	2020-06-11 14:02:23.339661+03	\N	30
328	cert qqq	description qqq	100	2020-06-11 14:02:27.923867+03	\N	30
329	cert qqqq	description qqqq	100	2020-06-11 14:02:32.248659+03	\N	30
330	cert 1111	description 1111	100	2020-06-11 14:02:44.551911+03	\N	30
331	cert 1111	description 1111	100	2020-06-11 14:02:59.344046+03	\N	30
\.


--
-- Name: certificate_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.certificate_id_seq', 331, true);


--
-- Data for Name: certificate_tag; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.certificate_tag (certificate_id, tag_id) FROM stdin;
280	95
279	97
280	101
320	100
319	100
321	100
322	100
323	100
324	102
325	103
\.


--
-- Data for Name: tag; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tag (id, name) FROM stdin;
99	EtestTag 6
96	EtestTag 3
101	Tag_2020
102	tag abcdefgh
103	tag abcdefghj
95	AtestTag 2
98	BtestTag 5
100	CtestTag 7
97	DtestTag 4
\.


--
-- Name: tag_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tag_id_seq', 103, true);


--
-- Name: certificate certificate_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate
    ADD CONSTRAINT certificate_pk PRIMARY KEY (id);


--
-- Name: certificate_tag certificate_tag_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate_tag
    ADD CONSTRAINT certificate_tag_pk PRIMARY KEY (certificate_id, tag_id);


--
-- Name: tag tag_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pk PRIMARY KEY (id);


--
-- Name: certificate row_mod_on_certificate_trigger_; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER row_mod_on_certificate_trigger_ BEFORE INSERT OR UPDATE ON public.certificate FOR EACH ROW EXECUTE PROCEDURE public.update_row_modified_function_();


--
-- Name: certificate_tag cert_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate_tag
    ADD CONSTRAINT cert_fk FOREIGN KEY (certificate_id) REFERENCES public.certificate(id);


--
-- Name: certificate_tag tag_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate_tag
    ADD CONSTRAINT tag_fk FOREIGN KEY (tag_id) REFERENCES public.tag(id);


--
-- PostgreSQL database dump complete
--

