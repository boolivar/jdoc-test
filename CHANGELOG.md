# Changelog

## [0.9.0](https://github.com/boolivar/jdoc-test/compare/0.8.1...0.9.0) (2024-08-27)


### Features

* maven central upload ([#32](https://github.com/boolivar/jdoc-test/issues/32)) ([cfd2219](https://github.com/boolivar/jdoc-test/commit/cfd2219c65517cbc887c1ae2ed36759530733b4a))

## [0.8.1](https://github.com/boolivar/jdoc-test/compare/0.8.0...0.8.1) (2024-05-16)


### Bug Fixes

* removed jdoc-platform publication from module metadata ([d490322](https://github.com/boolivar/jdoc-test/commit/d49032222cc3e391fe5062ab9f092e674d36e221))

## [0.8.0](https://github.com/boolivar/jdoc-test/compare/0.7.0...0.8.0) (2024-05-15)


### Features

* jdoc-spock gradle plugin ([#24](https://github.com/boolivar/jdoc-test/issues/24)) ([0188932](https://github.com/boolivar/jdoc-test/commit/01889324d4b9f8d4e46e6da2749e9efd858dd460))
* JdocSpockPlugin byteBuddyVersion 1.14.15 ([e51bcb7](https://github.com/boolivar/jdoc-test/commit/e51bcb7c69ddbd7c2ce0af0aa817a19e18360fd4))
* packages renamed in order to relocate on shadow and eliminate class conflicts between plugins ([f2f2524](https://github.com/boolivar/jdoc-test/commit/f2f2524fbeadfe59a56d65bbe6ad069c3ca7a74a))
* shadow direct dependencies of projects on gradle.projectsEvaluated ([e3fa4aa](https://github.com/boolivar/jdoc-test/commit/e3fa4aa5146f0fc2aafc609528ccefaf4e84c28e))


### Bug Fixes

* configure jdocSpock source set dependencies in JdocSpockPlugin ([11390cf](https://github.com/boolivar/jdoc-test/commit/11390cf80116c6b89621e76e90a8690bc6388959))
* public SOURCE_SET_NAME for JdocSpockPlugin ([bc0bb12](https://github.com/boolivar/jdoc-test/commit/bc0bb120a2fd67c97a1f042d00d74a1043ed60fd))
* removed reports dir output for jdocCucumberTest gradle task ([ef3687c](https://github.com/boolivar/jdoc-test/commit/ef3687ca379dda49b59fdebb28e11421373d6546))

## [0.7.0](https://github.com/boolivar/jdoc-test/compare/0.6.0...0.7.0) (2024-05-06)


### Features

* introduce jdoc-junit-engine-commons module in order to remove junit dependency from jdoc-core and gradle plugin ([004f8b0](https://github.com/boolivar/jdoc-test/commit/004f8b00cf3fe0e07e1b13a9c86cdee5a82152bc))
* project group changed to io.github.boolivar.jdoctest ([c0f61dd](https://github.com/boolivar/jdoc-test/commit/c0f61dda7bd6615ac2ad6543a4455ba48fc245a0))
* removed apache commons dependency from jdoc-core ([5ca99e2](https://github.com/boolivar/jdoc-test/commit/5ca99e24bea7bcdb6f78c93151bfe81611611637))
* removed commons-io dependency from gradle plugin ([35b0184](https://github.com/boolivar/jdoc-test/commit/35b01847a70518cd7f64a49e6a7b1a2a92bb4a7f))


### Bug Fixes

* shadow jdoc-cucumber-gradle-plugin project dependencies ([7c563b0](https://github.com/boolivar/jdoc-test/commit/7c563b0a39a61272549ab02da96c3dcdc8c04efa))
* versionMapping fromResolutionResult for jdoc-cucumber-gradle-plugin publication ([227f110](https://github.com/boolivar/jdoc-test/commit/227f110cb4db059f84fa7431ae95c7bec5bda044))

## [0.6.0](https://github.com/boolivar/jdoc-test/compare/0.5.0...0.6.0) (2024-05-03)


### Features

* cucumber gradle plugin ([#18](https://github.com/boolivar/jdoc-test/issues/18)) ([beda224](https://github.com/boolivar/jdoc-test/commit/beda224a93c8b1fed607c584ac3c9870464b2376))


### Bug Fixes

* dir config for GroovyCompiler spock test to avoid storing generated Hello class in the working copy ([fc424dc](https://github.com/boolivar/jdoc-test/commit/fc424dcec1e68f5d2cf2386d3dee968d9457fdd4))
* RegexParser lazy matcher to handle multiple tags on single line ([2d17147](https://github.com/boolivar/jdoc-test/commit/2d17147b51a0fbf6bd225ebc210f6324a026a06c))
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
