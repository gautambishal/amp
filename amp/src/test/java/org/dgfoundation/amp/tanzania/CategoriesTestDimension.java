package org.dgfoundation.amp.tanzania;

import java.util.Arrays;
import java.util.List;
import org.dgfoundation.amp.testmodels.dimensions.HardcodedNiDimension;
import org.dgfoundation.amp.testmodels.dimensions.HNDNode;
import static org.dgfoundation.amp.testmodels.dimensions.HNDNode.element;

public class CategoriesTestDimension extends HardcodedNiDimension {

	public CategoriesTestDimension(String name, int depth) {
		super(name, depth);
	}

	public final static CategoriesTestDimension instance = new CategoriesTestDimension("cats", 2);

	@Override
	protected List<HNDNode> buildHardcodedElements() {
		return Arrays.asList(
		element(2, "Accession Instrument", 
			element(5, "IPA" ), 
			element(6, "CARDS" ) ), 
		element(3, "Logframe", 
			element(7, "None" ), 
			element(8, "Objective" ), 
			element(9, "Purpose" ), 
			element(45, "Results" ) ), 
		element(5, "Document type", 
			element(46, "Project Document and Budget" ), 
			element(50, "Strategy and Frameworks" ), 
			element(51, "Procedures" ), 
			element(53, "Terms of Reference" ), 
			element(54, "Contracts" ), 
			element(58, "Reports" ) ), 
		element(6, "Activity Status", 
			element(63, "Planned" ), 
			element(64, "Ongoing" ), 
			element(65, "Closed" ), 
			element(66, "Cancelled" ) ), 
		element(7, "Implementation Level", 
			element(69, "Regional" ), 
			element(70, "National" ), 
			element(71, "Both" ) ), 
		element(8, "Team Type", 
			element(72, "Bilateral" ), 
			element(73, "Multilateral" ) ), 
		element(10, "Implementation Location", 
			element(76, "Country" ), 
			element(77, "Region" ), 
			element(119, "Zone" ), 
			element(129, "District" ) ), 
		element(11, "Type of Assistance", 
			element(80, "Grant" ), 
			element(81, "In-kind" ), 
			element(82, "Loan" ), 
			element(121, "Technical Assistance" ) ), 
		element(12, "Financing Instrument", 
			element(84, "General Budget Support (GBS)" ), 
			element(85, "Sector Budget Support (SBS)" ), 
			element(86, "Basket Funds (BF)" ), 
			element(87, "Direct Project Funds (DPF)" ) ), 
		element(13, "Program Type", 
			element(90, "National" ), 
			element(91, "Regional" ), 
			element(92, "Sectoral" ) ), 
		element(14, "Financial Instrument", 
			element(101, "GBS" ), 
			element(103, "SBS" ), 
			element(105, "Basket" ), 
			element(107, "DPS on Budget" ) ), 
		element(17, "MTEF Projection", 
			element(111, "projection" ), 
			element(113, "pipeline" ) ), 
		element(18, "Document Languages", 
			element(114, "English" ), 
			element(115, "French" ) ), 
		element(20, "Activity Level", 
			element(116, "Level 1" ), 
			element(117, "Level 2" ), 
			element(118, "Level 3" ) ), 
		element(24, "Project Category", 
			element(122, "Government" ), 
			element(123, "Independent Structure Under Government Control/Supervision" ), 
			element(124, "Independent Executing Agency (not related to the Government)" ), 
			element(136, "Local Non Government Organizations (NGOs)" ), 
			element(137, "International Non Government Organizations (INGOs)" ), 
			element(138, "Civil Society Organizations (CSOs)" ), 
			element(139, "Private Sector" ) ), 
		element(25, "A.C. Chapter", 
			element(125, "Budget" ), 
			element(126, "Dedicated" ), 
			element(127, "Project" ) ), 
		element(26, "IPA Activity Type", 
			element(128, "Activity 1" ) ), 
		element(27, "Funding Status", 
			element(220, "Arusha" ), 
			element(221, "Coast" ), 
			element(222, "Dar-es-Salaam" ), 
			element(223, "Dodoma" ), 
			element(224, "Iringa" ), 
			element(225, "Kagera" ), 
			element(226, "Kigoma" ), 
			element(227, "Kilimanjaro" ), 
			element(228, "Lindi" ), 
			element(229, "Manyara" ), 
			element(230, "Mara" ), 
			element(231, "Mbeya" ), 
			element(232, "Morogoro" ), 
			element(233, "Mtwara" ), 
			element(234, "Mwanza" ), 
			element(235, "Njombe Region" ), 
			element(236, "Pwani" ), 
			element(237, "Rukwa" ), 
			element(238, "Ruvama" ), 
			element(239, "Shinyanga" ), 
			element(240, "Singida" ), 
			element(241, "Tabora" ), 
			element(242, "Tanga" ), 
			element(243, "Zanzibar" ), 
			element(349, "Simiyu Region" ) ), 
		element(29, "Mode of Payment", 
			element(140, "Direct Payment" ), 
			element(141, "Direct Funding" ), 
			element(244, "Arumeru DC" ), 
			element(245, "Arusha DC" ), 
			element(246, "Arusha MC" ), 
			element(247, "Karatu DC" ), 
			element(248, "Longido DC" ), 
			element(249, "Meru DC" ), 
			element(250, "Monduli DC" ), 
			element(251, "Ngorongoro DC" ), 
			element(252, "Bagamoyo DC" ), 
			element(253, "Kibaha DC" ), 
			element(254, "Kibaha TC" ), 
			element(255, "Kisarawe DC" ), 
			element(256, "Mafia DC" ), 
			element(257, "Mkuranga DC" ), 
			element(258, "Rufiji DC" ), 
			element(259, "Dar es Salaam CC" ), 
			element(260, "Ilala MC" ), 
			element(261, "Kinondoni MC" ), 
			element(262, "Temeke MC" ), 
			element(263, "Bahi DC" ), 
			element(264, "Chamwino DC" ), 
			element(265, "Dodoma DC" ), 
			element(266, "Dodoma MC" ), 
			element(267, "Kondoa DC" ), 
			element(268, "Knogwa DC" ), 
			element(269, "Mpwapwa DC" ), 
			element(270, "Iringa DC" ), 
			element(271, "Ininga MC" ), 
			element(272, "Iringa RAS" ), 
			element(273, "Kilolo DC" ), 
			element(274, "Ludewa DC" ), 
			element(275, "Makete DC" ), 
			element(276, "Mufindi DC" ), 
			element(277, "Njombe DC" ), 
			element(278, "Njombe TC" ), 
			element(279, "Biharamulo DC" ), 
			element(280, "Bukoba DC" ), 
			element(281, "Bukoba MC" ), 
			element(282, "Chato DC" ), 
			element(283, "Karagwe DC" ), 
			element(284, "Misenyi DC" ), 
			element(285, "Muleba DC" ), 
			element(286, "Ngara DC" ), 
			element(287, "Kasulu DC" ), 
			element(288, "Kibondo DC" ), 
			element(289, "Kigoma DC" ), 
			element(290, "Kigoma/Ujiji MC" ), 
			element(291, "Hai DC" ), 
			element(292, "Moshi DC" ), 
			element(293, "Moshi MC" ), 
			element(294, "Mwanga DC" ), 
			element(295, "Rombo DC" ), 
			element(296, "Same DC" ), 
			element(297, "Siha DC" ), 
			element(298, "Kilwa DC" ), 
			element(299, "Lindi DC" ), 
			element(300, "Lindi TC" ), 
			element(301, "Liwale DC" ), 
			element(302, "Nachingwea DC" ), 
			element(303, "Ruangwa DC" ), 
			element(304, "Babati DC" ), 
			element(305, "Babati TC" ), 
			element(306, "Hanang DC" ), 
			element(307, "Kitelo DC" ), 
			element(308, "Mbulu DC" ), 
			element(309, "Simanjiro DC" ), 
			element(310, "Bunda DC" ), 
			element(311, "Musoma DC" ), 
			element(312, "Musoma MC" ), 
			element(313, "Rorya DC" ), 
			element(314, "Serengeti DC" ), 
			element(315, "Tarime DC" ), 
			element(316, "Chunya DC" ), 
			element(317, "Ileje DC" ), 
			element(318, "Kyela DC" ), 
			element(319, "Mbarali DC" ), 
			element(320, "Mbeya CC" ), 
			element(321, "Mbeya DC" ), 
			element(322, "Mbozi DC" ), 
			element(323, "Rungwe DC" ), 
			element(324, "Kilombero DC" ), 
			element(325, "Kilosa DC" ), 
			element(326, "Morogoro DC" ), 
			element(327, "Morogoro MC" ), 
			element(328, "Mvomero DC" ), 
			element(329, "Ulanga DC" ), 
			element(330, "Masasi DC" ), 
			element(331, "Mtwara DC" ), 
			element(332, "Mtwara MC" ), 
			element(333, "Nanyumbu DC" ), 
			element(334, "Newala DC" ), 
			element(335, "Tandahimba DC" ), 
			element(336, "Geita DC" ), 
			element(337, "Kwimba DC" ), 
			element(338, "Magu DC" ), 
			element(339, "Misungwi DC" ), 
			element(340, "Mwanza CC" ), 
			element(341, "Sengerema DC" ), 
			element(342, "Ukerewe DC" ), 
			element(343, "Njombe RAS" ), 
			element(344, "Mpanda DC" ), 
			element(345, "Mpanda TC" ), 
			element(346, "Nkansi DC" ), 
			element(347, "Sumbawanga DC" ), 
			element(348, "Sumbawanga MC" ), 
			element(350, "Mbinga DC" ), 
			element(351, "Namtumbo DC" ), 
			element(352, "Songea DC" ), 
			element(353, "Songea MC" ), 
			element(354, "Tunduru DC" ), 
			element(355, "Bariadi DC" ), 
			element(356, "Bukombe DC" ), 
			element(357, "Kahama DC" ), 
			element(358, "Kishapu DC" ), 
			element(359, "Maswa DC" ), 
			element(360, "Meatu DC" ), 
			element(361, "Shinyanga DC" ), 
			element(362, "Shinyanga MC" ), 
			element(363, "Iramba DC" ), 
			element(364, "Manyoni DC" ), 
			element(365, "Singida DC" ), 
			element(366, "Singida MC" ), 
			element(367, "Igunga DC" ), 
			element(368, "Nzega DC" ), 
			element(369, "Sikonge DC" ), 
			element(370, "Tabora MC" ), 
			element(371, "Tabora/Uyui DC" ), 
			element(372, "Urambo DC" ), 
			element(373, "Handeni DC" ), 
			element(374, "Kilindi DC" ), 
			element(375, "Korogwe DC" ), 
			element(376, "Lushoto DC" ), 
			element(377, "Mkinga DC" ), 
			element(378, "Muheza DC" ), 
			element(379, "Pangani DC" ), 
			element(380, "Tanga CC" ), 
			element(381, "North Pemba" ), 
			element(382, "North Unguja" ), 
			element(383, "South Pemba" ), 
			element(384, "South Unguja" ), 
			element(385, "Urban West- Unguja" ) ), 
		element(30, "Pledges Types", 
			element(143, "Reprogrammed funds" ), 
			element(144, "New funds" ) ), 
		element(31, "Event type", 
			element(157, "Mission" ), 
			element(158, "Program Review" ), 
			element(159, "Budget Review" ), 
			element(160, "Blackout period" ), 
			element(161, "Workshop" ), 
			element(162, "AMP Training" ) ), 
		element(32, "Colors", 
			element(145, "aqua" ), 
			element(146, "black" ), 
			element(147, "blue" ), 
			element(148, "fuchsia" ), 
			element(149, "silver" ), 
			element(150, "green" ), 
			element(151, "lime" ), 
			element(152, "maroon" ), 
			element(153, "navy" ), 
			element(154, "purple" ), 
			element(155, "teal" ), 
			element(156, "yellow" ) ), 
		element(33, "NGO Budget Type", 
			element(164, "Annual budget of internal/administrative functioning" ), 
			element(165, "Total Amount" ), 
			element(166, "Organization personal resources" ), 
			element(167, "Donors resources" ) ), 
		element(34, "Procurement System", 
			element(171, "Use donor's own system" ), 
			element(172, "Use Nepalese system" ), 
			element(173, "Rely on other donor's system" ) ), 
		element(35, "Reporting System", 
			element(174, "Use donor's own system" ), 
			element(175, "Use Nepalese system" ), 
			element(176, "Rely on other donor's system" ) ), 
		element(36, "Audit System", 
			element(177, "Use donor's own system" ), 
			element(178, "Use Nepalese system" ), 
			element(179, "Rely on other donor's system" ) ), 
		element(37, "Institutions", 
			element(180, "Use donor's own structure" ), 
			element(181, "Create new parallel institution" ), 
			element(182, "Use existing Nepalese institutions" ), 
			element(183, "Rely on other donor's institutions" ) ), 
		element(38, "Phone Type", 
			element(184, "Home" ), 
			element(185, "Cell" ), 
			element(186, "Work" ) ), 
		element(39, "Contact Title", 
			element(190, "Mr" ), 
			element(191, "Ms" ), 
			element(192, "Mrs" ), 
			element(193, "Dr" ) ), 
		element(40, "Project Implementing Unit", 
			element(187, "Parallel Project Implementing" ), 
			element(188, "Embedded Project Implementing" ), 
			element(189, "No Project Implementing Unit" ) ), 
		element(42, "Activity Budget", 
			element(194, "Off Budget" ), 
			element(195, "On Budget" ), 
			element(196, "Treasury" ) ), 
		element(43, "Adjustment Type", 
			element(197, "Planned" ), 
			element(198, "Actual" ) ), 
		element(44, "Workspace Group", 
			element(200, "Government" ), 
			element(201, "Line Ministries" ), 
			element(202, "Donor" ) ), 
		element(45, "Report Categories", 
			element(203, "On budget" ), 
			element(204, "Off budget" ) ), 
		element(46, "Transaction Type", 
			element(205, "Commitments" ), 
			element(206, "Disbursement Orders" ), 
			element(207, "Disbursements" ), 
			element(208, "Expenditures" ) ), 
		element(47, "Modalities", 
			element(386, "Diplomats and courses" ), 
			element(387, "Conferences, seminars, capacity specializations" ), 
			element(388, "Interchanging models, proposals, and printed materials" ), 
			element(389, "Country Experiences" ), 
			element(390, "Internships" ), 
			element(391, "Scholarships" ), 
			element(392, "Virtual Platforms and blogs to consult, learn, and exchange ideas" ), 
			element(393, "Videoconference and studying abroad" ), 
			element(394, "Sending and exchanging experts, researchers, and professors" ), 
			element(395, "Development of shared analytical studies" ), 
			element(396, "Industry University Cooperation" ) ), 
		element(48, "Type of Cooperation", 
			element(397, "Official Development Aid (ODA)" ), 
			element(398, "Bilateral South South Cooperation" ), 
			element(399, "Triangular South South Cooperation" ), 
			element(400, "Regional South South Cooperation" ) ), 
		element(49, "Type of Implementation", 
			element(401, "Program" ), 
			element(402, "Project" ), 
			element(403, "Action" ) ), 
		element(50, "Workspace Prefix", 
			element(404, "SSC_" ) ), 
		element(51, "Project Implementation Mode", 
			element(405, "Direct implementation through non-gov't entity" ), 
			element(406, "Joint implementation through gov't and non-gov't entity" ), 
			element(407, "National implementation throug gov't entity" ) ), 
		element(52, "Budget Structure", 
			element(408, "Salaries" ), 
			element(409, "Operations" ), 
			element(410, "Capital" ) ), 
		element(53, "Expenditure Class", 
			element(411, "Capital Expenditure" ), 
			element(412, "Compensation / Salaries" ), 
			element(413, "Goods and Services" ), 
			element(414, "Others" ) ), 
		element(54, "Indicator Layer Type", 
			element(415, "Per Capita" ), 
			element(416, "Ratio (% of Total Population)" ), 
			element(417, "Ratio (other)" ), 
			element(418, "Count" ) ));
	}
}

