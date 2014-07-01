package de.dhbw.horb.routePlanner.parser;
//	public void updateWays() {
//
//		try {
//			for (int i = 0; i < listWay.size(); i++) {
//				Element elWay = (Element) (listWay.get(i));
//
//				if (null == elWay)
//					continue;
//
//				String distance = elWay.getAttributeValue(Constants.WAY_DISTANCE);
//				String maxspeed = elWay.getAttributeValue(Constants.WAY_MAXSPEED);
//				String ref = elWay.getAttributeValue(Constants.WAY_REF);
//				Boolean isLink = false;
//
//				if (distance != null && maxspeed != null && ref != null)
//					continue;
//
//				List<Element> listTag = elWay.getChildren(Constants.WAY_TAG);
//
//				isLink = isLink(listTag);
//
//				if (!isLink && distance == null)
//					elWay.setAttribute(Constants.WAY_DISTANCE, getDistanceFromWay(elWay).toString());
//
//				if (!isLink && maxspeed == null)
//					elWay.setAttribute(Constants.WAY_MAXSPEED, getMaxSpeed(listTag));
//
//				if (!isLink && ref == null)
//					elWay.setAttribute(Constants.WAY_REF, getRef(listTag));
//
//				if (isLink) {
//					deleteWay(listWay, i);
//					i--;
//				} else {
//					deleteAllTags(listTag);
//				}
//			}
//
//			outp.output(xmlDocGraphData, new FileOutputStream(new File(Constants.XML_GRAPHDATA)));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//
//	private void deleteWay(List<Element> ways, int index) {
//		ways.remove(index);
//	}
//
//	private String getRef(List<Element> tags) {
//
//		for (int i = 0; i < tags.size(); i++) {
//			Element elTag = (Element) (tags.get(i));
//			if (elTag == null)
//				continue;
//
//			String maxspeed = getAttributeValueForK(elTag, Constants.WAY_MAXSPEED);
//
//			if (maxspeed != null)
//				return maxspeed;
//		}
//		return "";
//	}
//
//	private String getMaxSpeed(List<Element> tags) {
//
//		for (int i = 0; i < tags.size(); i++) {
//			Element elTag = (Element) (tags.get(i));
//			if (elTag == null)
//				continue;
//
//			String ref = getAttributeValueForK(elTag, Constants.WAY_REF);
//
//			if (ref != null)
//				return ref;
//		}
//		return "";
//	}
//
//	private Double getDistanceFromWay(Element way) {
//
//		Double km = 0.0;
//
//		List<Element> listNode = way.getChildren(Constants.WAY_NODE);
//		for (int x = 0; x < (listNode.size() - 1); x++) {
//			Element elNode = (Element) (listNode.get(x));
//			if (elNode == null)
//				continue;
//
//			Long id1 = Long.valueOf(elNode.getAttributeValue(Constants.WAY_REF));
//
//			elNode = (Element) (listNode.get(x + 1));
//			if (elNode == null)
//				continue;
//
//			Long id2 = Long.valueOf(elNode.getAttributeValue(Constants.WAY_REF));
//
//			// km += gmp.fromLatLonToDistanceInKM(getNode(id1), getNode(id2));
//		}
//
//		return km;
//	}
//
//}
