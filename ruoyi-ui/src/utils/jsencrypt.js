import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

// const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdH\n' +
//   'nzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ=='

// const privateKey = 'MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAqhHyZfSsYourNxaY\n' +
//   '7Nt+PrgrxkiA50efORdI5U5lsW79MmFnusUA355oaSXcLhu5xxB38SMSyP2KvuKN\n' +
//   'PuH3owIDAQABAkAfoiLyL+Z4lf4Myxk6xUDgLaWGximj20CUf+5BKKnlrK+Ed8gA\n' +
//   'kM0HqoTt2UZwA5E2MzS4EI2gjfQhz5X28uqxAiEA3wNFxfrCZlSZHb0gn2zDpWow\n' +
//   'cSxQAgiCstxGUoOqlW8CIQDDOerGKH5OmCJ4Z21v+F25WaHYPxCFMvwxpcw99Ecv\n' +
//   'DQIgIdhDTIqD2jfYjPTY8Jj3EDGPbH2HHuffvflECt3Ek60CIQCFRlCkHpi7hthh\n' +
//   'YhovyloRYsM+IS9h/0BzlEAuO0ktMQIgSPT3aFAgJYwKpqRYKlLDVcflZFCKY7u3\n' +
//   'UP8iWi1Qw0Y='

const publicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg1vVyb3PPzf777yucIPZql1Vd+NMaNvcXui5BENYBYrz1Pc8mDGFVS6YZlsD2mCUgOfeWhbFeBVWIRW9i81uez+0GJLL7QB2iq0dGZFSQgWruTWz0G/Xdlp7rT+KA+p0ApTLbeSegod6dpC+ocoCfrDll1NBzQlM5fp6VCarI9NAUI3F64lz1b90FQfIdNTG6B0P7EhNfRAV/JdngIc3jBQgj0hN44pHkWd9GU/rnG5ZWbXnOlRLN/jQSZHHewToiEMrh5jgcncE1rcF2oHvXjplT6IFtr2SZPUlqvGwyTlXOutFx82dDsdf2IddKiAezhnYwBu+2DVSMaK0USY+9wIDAQAB'

const privateKey = 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCDW9XJvc8/N/vvvK5wg9mqXVV340xo29xe6LkEQ1gFivPU9zyYMYVVLphmWwPaYJSA595aFsV4FVYhFb2LzW57P7QYksvtAHaKrR0ZkVJCBau5NbPQb9d2WnutP4oD6nQClMtt5J6Ch3p2kL6hygJ+sOWXU0HNCUzl+npUJqsj00BQjcXriXPVv3QVB8h01MboHQ/sSE19EBX8l2eAhzeMFCCPSE3jikeRZ30ZT+ucbllZtec6VEs3+NBJkcd7BOiIQyuHmOBydwTWtwXage9eOmVPogW2vZJk9SWq8bDJOVc660XHzZ0Ox1/Yh10qIB7OGdjAG77YNVIxorRRJj73AgMBAAECggEAeu62HBCD5SEqqUkGY4wxZ4vjkchp2PRxv2zCweeYd897tEIRNkmaA4cQ3zGsYHHkaOMWp28KLQCi02G6OxztmXCbxieTq2C0j8HFKwliVyFhfXpxps8UcNHrTs7ZBiMjoMrJuzWq1nOdu1po3ZblUNWSeFVVjmJ20f/g/eIWZfZD9V431DBtM0dNRe+Pnvis+MNH4EniLmyT1R+dP5OiUMVK1uEqPpXsKgslog6zqKgkoD9ShQNuTFqrYNA/RTWlAwGpy92RpsvToXAhFtMcfI8/jA16J4CNHiZCgIlKIv9MQn8+odp9RGtPcP68FBTCS4SxhRHeeI69Otxd8lYBoQKBgQD8zQnBgdzinAJj1duMdnaIMj1riRIVLW7qYjIGgxzTUg99EMY0HOv0Yn1oKJUSzCJEhuz+2szcfEdH4llTPxTygUf927D1eD/rwTrbBfUuhGRBpBJjeMpVaMG5jgOXNL0YXUn48iow8+nuSHUrryI9qKho25VAqTzERiBUQ6U2MQKBgQCFBWDtF2AjAGDSRFp0RdMenG2g3/jD4LmVJYDJnOxMH+hZbl8rIuQVwFy6iDRsHmNf+TTCTjL1iihITEIqm7Ekc4RyjC6325F1LkM0ElGtpeSY74VflITQbTAPQIwRbEanxBVfYQBbz25liVEgqQXGZgW1J+ypBeG+pNvXE/P1pwKBgBRvtuQvzV18Yy0S8sauJPLprNFuqUmQ/LBx871XCBOFRL9FheBcni6Jb73l2fdFDVgg2DPaGEmSumhzQela5iSckudQxUNyzygN3OYr2yBBj6FG1D26XBD/gPmxI1Ur7EV19S7s1MBUV4rtzJsVc10sr0Caz7wEcmcjjCWDum1hAoGAHe4Z4v7xeK6rGmSa3bKgUDDuUdZTSO+VkkN/+N1AJdjAFO9KppARNfu0YZwn/3PO9FdEUbv6p1jQu4O9F400jHkZghScpm3XCsDACkVc7292QhsSrXDjeWo3+BaT7Y+pTjJGhNF0nTPNKTd+c5TUYdGu0xYKn/Vc1+KMPJaWu90CgYAJqUNFqUAzmhrASmSbezWH3D4+DwBuRlCsIABYR/AtCgvVH2PyKpgrWkFIvsOjYXRpo0pv+edkJeAsLMSBPtbJOLx6IMjd7iZrUWRCJHffVn79QUP0kYiYvLFiwfHkQCyafUmjNDrtZqIU9eZigJxYeXro6ArXBQbjU39qBO4poQ=='

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

