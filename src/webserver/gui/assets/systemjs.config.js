(function(global) {

  var config = {
    map: map,
    baseURL: '/gui/',
    packages: packages
  };

  var d3Modules = [
    'd3',
    'd3-array',
    'd3-axis',
    'd3-brush',
    'd3-chord',
    'd3-collection',
    'd3-color',
    'd3-dispatch',
    'd3-drag',
    'd3-dsv',
    'd3-ease',
    'd3-force',
    'd3-format',
    'd3-geo',
    'd3-hierarchy',
    'd3-interpolate',
    'd3-path',
    'd3-polygon',
    'd3-quadtree',
    'd3-queue',
    'd3-random',
    'd3-request',
    'd3-scale',
    'd3-selection',
    'd3-shape',
    'd3-time',
    'd3-timer',
    'd3-time-format',
    'd3-transition',
    'd3-voronoi',
    'd3-zoom',
  ];

  var map = {
    'app': '.',
    '@angular': './node_modules/@angular',
    'rxjs': './node_modules/rxjs',
    'ng2-nvd3': './node_modules/ng2-nvd3/build'
  };

  var packages = {
    'app': {
      main: 'main.js',
      defaultExtension: 'js'
    },
    'rxjs': {
      defaultExtension: 'js'
    },
    'ng2-nvd3': {
      main: 'index.js',
      defaultExtension: 'js'
    }
  };

  for (var i = 0; i < d3Modules.length; i++) {
    map[d3Modules[i]] =  './node_modules/' + d3Modules[i];
    packages[d3Modules[i]] = {
      main: 'index.js',
      defaultExtension: 'js'
    };
  };

  var ngPackageNames = [
    'animations',
    'common',
    'compiler',
    'core',
    'forms',
    'http',
    'platform-browser',
    'platform-browser-dynamic',
    'router',
    'router-deprecated',
    'upgrade'
  ];

  ngPackageNames.forEach(function (pkgName) {
      packages['@angular/'+pkgName] = {
        main: 'index.js',
        defaultExtension: 'js'
      };
  });

  function packUmd(pkgName) {
    packages['@angular/'+pkgName] = {
      main: '/bundles/' + pkgName + '.umd.js',
      defaultExtension: 'js'
    };
  }

  var setPackageConfig = System.packageWithIndex ? packIndex : packUmd;

  ngPackageNames.forEach(setPackageConfig);

  var config = {
    map: map,
    packages: packages,
    transpiler: 'plugin-babel'
  };

  System.config(config);
})(this);
