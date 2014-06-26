package com.google.maps.googleMapsAPI;


public final class GoogleMapsProjection2 
{
    private final int TILE_SIZE = 256;
    private PointF _pixelOrigin;
    private double _pixelsPerLonDegree;
    private double _pixelsPerLonRadian;

    public GoogleMapsProjection2()
    {
        this._pixelOrigin = new PointF(TILE_SIZE / 2.0,TILE_SIZE / 2.0);
        this._pixelsPerLonDegree = TILE_SIZE / 360.0;
        this._pixelsPerLonRadian = TILE_SIZE / (2 * Math.PI);
    }

    public double bound(double val, double valMin, double valMax)
    {
        double res;
        res = Math.max(val, valMin);
        res = Math.min(val, valMax);
        return res;
    }

    public double degreesToRadians(double deg) 
    {
        return deg * (Math.PI / 180);
    }

    public double radiansToDegrees(double rad) 
    {
        return rad / (Math.PI / 180);
    }

    public PointF fromLatLngToPoint(double lat, double lng, int zoom)
    {
        PointF point = new PointF(0, 0);

        point.x = _pixelOrigin.x + lng * _pixelsPerLonDegree;       

        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
        // about a third of a tile past the edge of the world tile.
        double siny = bound(Math.sin(degreesToRadians(lat)), -0.9999,0.9999);
        point.y = _pixelOrigin.y + 0.5 * Math.log((1 + siny) / (1 - siny)) *- _pixelsPerLonRadian;

        int numTiles = 1 << zoom;
        point.x = point.x * numTiles;
        point.y = point.y * numTiles;
        return point;
     }

    public PointF fromPointToLatLng(PointF point, int zoom)
    {
        int numTiles = 1 << zoom;
        point.x = point.x / numTiles;
        point.y = point.y / numTiles;       

        double lng = (point.x - _pixelOrigin.x) / _pixelsPerLonDegree;
        double latRadians = (point.y - _pixelOrigin.y) / - _pixelsPerLonRadian;
        double lat = radiansToDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
        return new PointF(lat, lng);
    }
    
    public double fromLatLonToDistanceInKM(PointF start, PointF end){
    
    	int R = 6371; // km
    	double phi1 = degreesToRadians(start.x);
    	double phi2 = degreesToRadians(end.x);
    	double deltaPhi = degreesToRadians(end.x-start.x);
    	double deltaLambda = degreesToRadians(end.y-start.y);

    	double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
    	        Math.cos(phi1) * Math.cos(phi2) *
    	        Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    	return R * c;

    }

}

