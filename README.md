# smart-owc-geojson
===================

OGC OWS Context GeoJSON Encoding Standard 1.0 (14-055r2) for Play Framework 2.5.x

The OGC OWS Context GeoJSON Encoding Standard was finalised and published 2017-04-06.

https://portal.opengeospatial.org/files/?artifact_id=68826

The current implementation (from November 2016) relied on preliminary GeoJson 
results from Atom encoding mappings from OWS-10 TestBed working groups during 
the last years, before an official OWC GeoJson encoding was standardised. 
We are currently not supporting the ATOM encoding.

- Referenced from: https://github.com/ZGIS/smart-csw-ingester/issues/32
- Referenced from: https://github.com/ZGIS/smart-portal-webgui/issues/63
- Referenced from: https://github.com/ZGIS/smart-portal-backend/issues/25   

## Build

[![Build Status][build-status-badge]][build-status-url]
[![Issues][issues-badge]][issues-url]
[![Bintray](https://img.shields.io/bintray/v/allixender/ivy2/smart-owc-geojson.svg)](https://bintray.com/allixender/ivy2/smart-owc-geojson)

[![License][license-badge]][license-url]
[![OpenHUB](https://www.openhub.net/p/smart-owc-geojson/widgets/project_thin_badge.gif)](https://www.openhub.net/p/smart-owc-geojson)
 
[build-status-badge]: https://img.shields.io/travis/ZGIS/smart-owc-geojson.svg?style=flat-square
[build-status-url]: https://travis-ci.org/ZGIS/smart-owc-geojson
[issues-badge]: https://img.shields.io/github/issues/ZGIS/smart-owc-geojson.svg?style=flat-square
[issues-url]: https://github.com/ZGIS/smart-owc-geojson/issues
[license-badge]: https://img.shields.io/badge/License-Apache%202-blue.svg?style=flat-square
[license-url]: LICENSE

<p><a href="https://api.travis-ci.org/repos/ZGIS/smart-owc-geojson/builds.atom"><img src="https://upload.wikimedia.org/wikipedia/en/4/43/Feed-icon.svg" align="left" height="32" width="32" alt="Builds Feed"></a></p>

[Site Docs](https://zgis.github.io/smart-owc-geojson/)

## Release on Bintray for resolving in Backend and CSW-Ingester

- https://bintray.com/allixender/ivy2/smart-owc-geojson

```scala
  // in build.sbt (important, might not be considered when in plugins.sbt)
  resolvers += Resolver.bintrayIvyRepo("allixender", "ivy2")
```

- If sbt-release complains about not tracked upstream:

```bash
git checkout dev || git checkout -b dev
git reset --hard origin/dev
git branch --set-upstream-to=origin/dev dev
git pull
```