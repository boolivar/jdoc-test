# Changelog

## [0.5.1](https://github.com/boolivar/jdoc-test/compare/0.5.0...v0.5.1) (2024-03-27)


### Bug Fixes

* release-please in ci workflow ([e0ad268](https://github.com/boolivar/jdoc-test/commit/e0ad268fb63bfbf6b1587172075b908851343fdb))
* target-branch, release-please v4.1.0 ([fb0cfbe](https://github.com/boolivar/jdoc-test/commit/fb0cfbe0180f10576fa728c66213b3f3dc5f08f3))

## [0.5.0](https://github.com/boolivar/jdoc-test/compare/0.4.0...0.5.0) (2024-03-24)


### Features

* Regex parser ([#11](https://github.com/boolivar/jdoc-test/issues/11)) ([7ecf325](https://github.com/boolivar/jdoc-test/commit/7ecf32568f4dfb43212334894354ab2109a3ae92))


### Bug Fixes

* jdoc-cucumber engine group + version/artifactId for all engines using manifest ([a8d9d23](https://github.com/boolivar/jdoc-test/commit/a8d9d23fdb0d20c80d825b68e7ec30a13785c305))

## [0.4.0](https://github.com/boolivar/jdoc-test/compare/0.3.0...0.4.0) (2024-03-22)


### Features

* jdoc-cucumber ([#9](https://github.com/boolivar/jdoc-test/issues/9)) ([47f7ebe](https://github.com/boolivar/jdoc-test/commit/47f7ebe053c62853fcd86eca558655f3de5c2cda))


### Bug Fixes

* subengine id to call spock engine ([aea9da3](https://github.com/boolivar/jdoc-test/commit/aea9da3dfebcff67f7fcc6c676228ce109ae96c6))

## [0.3.0](https://github.com/boolivar/jdoc-test/compare/0.2.0...0.3.0) (2024-03-19)


### Features

* no $target and mocks if no mockable constructor found ([34fe03d](https://github.com/boolivar/jdoc-test/commit/34fe03d2bee2e83837f5ed0350da68e2b148ada7))

## [0.2.0](https://github.com/boolivar/jdoc-test/compare/0.1.0...0.2.0) (2024-03-16)


### Features

* internal platform in order to not publish platform bom ([4c309ee](https://github.com/boolivar/jdoc-test/commit/4c309eed3edf89872af01750d639b1c70e8df704))


### Bug Fixes

* release-please CI workflow dependency ([a343a19](https://github.com/boolivar/jdoc-test/commit/a343a19495d6b70995a74df2a555d71c4ffb25b4))

## 0.1.0 (2024-03-15)


### Features

* close GroovyClassLoader ([efd2ad7](https://github.com/boolivar/jdoc-test/commit/efd2ad74709e86dcc0a029b5aac556a7bc42baa7))
* CodeBlockParser uses jsoup to parse nested &lt;code&gt; tags and consider lang attribute ([b993dbf](https://github.com/boolivar/jdoc-test/commit/b993dbf17a974da472a5134b5aad36b53371c786))
* CompilerConfigurationFactory ([26ba4e3](https://github.com/boolivar/jdoc-test/commit/26ba4e3626690e15b9da12244dccee7d0b95ec71))
* ConfigParams ([d2f2ecc](https://github.com/boolivar/jdoc-test/commit/d2f2ecc686b2dbdbe8b11e411b3cf883fccc7939))
* DiscoveryRequest implementation for EngineDiscoveryRequest ([77d887e](https://github.com/boolivar/jdoc-test/commit/77d887e641a3b06b35b53ef4daee3cdd5db3aa79))
* DiscoveryRequestMapper implementation ([4f20793](https://github.com/boolivar/jdoc-test/commit/4f207932bdd92ceee0b2a010501d70bce81e540f))
* GroovyCompiler ([2e17e5a](https://github.com/boolivar/jdoc-test/commit/2e17e5a54c40d20986ec669814770e0305c9e9d6))
* JavaFileParser ([3b2c189](https://github.com/boolivar/jdoc-test/commit/3b2c18953983902da3897ae0d8ca2ccc5705d13c))
* JavaFileParser block comments inclusion ([fa769b3](https://github.com/boolivar/jdoc-test/commit/fa769b3eefe8b42a42f5c55eee25e836c4248a2e))
* JavaFileSpecFactory ([5cc4282](https://github.com/boolivar/jdoc-test/commit/5cc4282f62cc3a55c774d055d02488fe9cd5eb85))
* jdoc-spock CodeBlockParser ([61d4f72](https://github.com/boolivar/jdoc-test/commit/61d4f722e148d50b7879d3fa162d9d6ebdad415c))
* JdocSpockEngine ([9e5a6d4](https://github.com/boolivar/jdoc-test/commit/9e5a6d47b84fde172e7838fc4d56d05fcd494dfa))
* junit test engine service provider configuration ([e7658b2](https://github.com/boolivar/jdoc-test/commit/e7658b2ce5ee956e98cae160aa1d2ecd741d81e2))
* mockable constructor search ([602b03c](https://github.com/boolivar/jdoc-test/commit/602b03cf85dc7d5e1fcf8a06198229d133861c58))
* public, protected and package visible constructors available for mocking ([fa30185](https://github.com/boolivar/jdoc-test/commit/fa30185012363a493655ffebb2102b6766353968))
* spec generation ([071cc03](https://github.com/boolivar/jdoc-test/commit/071cc03734d9dedebf703ca03490a89d902eefb5))
* SpecClassMapper ([0ca1ee0](https://github.com/boolivar/jdoc-test/commit/0ca1ee0e11c1f7c6ac507b2a5bfec8c71816bcd0))
* SpecClassMapperFactory ([ce08a99](https://github.com/boolivar/jdoc-test/commit/ce08a995faa5bca2ae2d380130f0886a65beea3b))
* SpockEngineService implementation ([e092fa1](https://github.com/boolivar/jdoc-test/commit/e092fa121bc9a5bb52bc0210abb6ca59283ce48c))


### Miscellaneous Chores

* release 0.1.0 ([12defd5](https://github.com/boolivar/jdoc-test/commit/12defd5b894539cd3ba455c013a0a995eaa53898))
