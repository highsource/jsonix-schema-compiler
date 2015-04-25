var PurchaseOrder_Module_Factory = function () {
  var PO = {
    n: 'PO',
    tis: [{
        ln: 'Items.Item',
        tn: null,
        ps: [{
            n: 'productName',
            en: {
              lp: 'productName'
            }
          }, {
            n: 'quantity',
            en: {
              lp: 'quantity'
            },
            ti: 'Int'
          }, {
            n: 'usPrice',
            en: {
              lp: 'USPrice'
            },
            ti: 'Decimal'
          }, {
            n: 'comment',
            en: {
              lp: 'comment'
            }
          }, {
            n: 'shipDate',
            en: {
              lp: 'shipDate'
            },
            ti: 'Calendar'
          }, {
            n: 'partNum',
            an: {
              lp: 'partNum'
            },
            t: 'a'
          }]
      }, {
        ln: 'PurchaseOrderType',
        ps: [{
            n: 'shipTo',
            en: {
              lp: 'shipTo'
            },
            ti: '.USAddress'
          }, {
            n: 'billTo',
            en: {
              lp: 'billTo'
            },
            ti: '.USAddress'
          }, {
            n: 'comment',
            en: {
              lp: 'comment'
            }
          }, {
            n: 'items',
            en: {
              lp: 'items'
            },
            ti: '.Items'
          }, {
            n: 'orderDate',
            ti: 'Calendar',
            an: {
              lp: 'orderDate'
            },
            t: 'a'
          }]
      }, {
        ln: 'USAddress',
        ps: [{
            n: 'name',
            en: {
              lp: 'name'
            }
          }, {
            n: 'street',
            en: {
              lp: 'street'
            }
          }, {
            n: 'city',
            en: {
              lp: 'city'
            }
          }, {
            n: 'state',
            en: {
              lp: 'state'
            }
          }, {
            n: 'zip',
            en: {
              lp: 'zip'
            },
            ti: 'Decimal'
          }, {
            n: 'country',
            an: {
              lp: 'country'
            },
            t: 'a'
          }]
      }, {
        ln: 'Items',
        ps: [{
            n: 'item',
            col: true,
            en: {
              lp: 'item'
            },
            ti: '.Items.Item'
          }]
      }],
    eis: [{
        en: {
          lp: 'purchaseOrder'
        },
        ti: '.PurchaseOrderType'
      }, {
        en: {
          lp: 'comment'
        }
      }]
  };
  return {
    PO: PO
  };
};
if (typeof define === 'function' && define.amd) {
  define([], PurchaseOrder_Module_Factory);
}
else {
  var PurchaseOrder_Module = PurchaseOrder_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.PO = PurchaseOrder_Module.PO;
  }
  else {
    var PO = PurchaseOrder_Module.PO;
  }
}