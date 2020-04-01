module.exports = {
  extends: 'airbnb-base',
  env: {
    jest: true,
    node: true,
  },
  rules: {
    'max-len': ['error', 200, { 'ignoreStrings': true }],
    'comma-dangle': ['error', 'never']
  }
};
