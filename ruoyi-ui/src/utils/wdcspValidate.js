/**
 * 电话/手机号验证
 * @param {string} url
 * @returns {Boolean}
 */
 export function validPhone(url) {
  const reg = /^([1]\d{10}|([\(（]?0[0-9]{2,3}[）\)]?[-]?)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)$/
  return reg.test(url)
}

/**
 * handle码验证
 * @param {string} url
 * @returns {Boolean}
 */
 export function validHandleCode(url) {
  const reg = /^86.*$/
  return reg.test(url)
}
