var PurchaseOrder_Module_Factory = function () {
  var PO = {
    name: 'PO',
    typeInfos: [{
        localName: 'Items.Item',
        typeName: null,
        propertyInfos: [{
            name: 'productName',
            elementName: {
              localPart: 'productName'
            }
          }, {
            name: 'quantity',
            elementName: {
              localPart: 'quantity'
            },
            typeInfo: 'Int'
          }, {
            name: 'usPrice',
            elementName: {
              localPart: 'USPrice'
            },
            typeInfo: 'Decimal'
          }, {
            name: 'comment',
            elementName: {
              localPart: 'comment'
            }
          }, {
            name: 'shipDate',
            elementName: {
              localPart: 'shipDate'
            },
            typeInfo: 'Calendar'
          }, {
            name: 'partNum',
            attributeName: {
              localPart: 'partNum'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'Items',
        propertyInfos: [{
            name: 'item',
            collection: true,
            elementName: {
              localPart: 'item'
            },
            typeInfo: '.Items.Item'
          }]
      }, {
        localName: 'USAddress',
        propertyInfos: [{
            name: 'name',
            elementName: {
              localPart: 'name'
            }
          }, {
            name: 'street',
            elementName: {
              localPart: 'street'
            }
          }, {
            name: 'city',
            elementName: {
              localPart: 'city'
            }
          }, {
            name: 'state',
            elementName: {
              localPart: 'state'
            }
          }, {
            name: 'zip',
            elementName: {
              localPart: 'zip'
            },
            typeInfo: 'Decimal'
          }, {
            name: 'country',
            attributeName: {
              localPart: 'country'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PurchaseOrderType',
        propertyInfos: [{
            name: 'shipTo',
            elementName: {
              localPart: 'shipTo'
            },
            typeInfo: '.USAddress'
          }, {
            name: 'billTo',
            elementName: {
              localPart: 'billTo'
            },
            typeInfo: '.USAddress'
          }, {
            name: 'comment',
            elementName: {
              localPart: 'comment'
            }
          }, {
            name: 'items',
            elementName: {
              localPart: 'items'
            },
            typeInfo: '.Items'
          }, {
            name: 'orderDate',
            typeInfo: 'Calendar',
            attributeName: {
              localPart: 'orderDate'
            },
            type: 'attribute'
          }]
      }],
    elementInfos: [{
        elementName: {
          localPart: 'comment'
        }
      }, {
        elementName: {
          localPart: 'purchaseOrder'
        },
        typeInfo: '.PurchaseOrderType'
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