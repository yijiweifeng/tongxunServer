/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $Writer = $protobuf.Writer, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.Msg = (function() {

    /**
     * Properties of a Msg.
     * @exports IMsg
     * @interface IMsg
     * @property {IHead|null} [head] Msg head
     * @property {string|null} [body] Msg body
     */

    /**
     * Constructs a new Msg.
     * @exports Msg
     * @classdesc Represents a Msg.
     * @implements IMsg
     * @constructor
     * @param {IMsg=} [properties] Properties to set
     */
    function Msg(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Msg head.
     * @member {IHead|null|undefined} head
     * @memberof Msg
     * @instance
     */
    Msg.prototype.head = null;

    /**
     * Msg body.
     * @member {string} body
     * @memberof Msg
     * @instance
     */
    Msg.prototype.body = "";

    /**
     * Creates a new Msg instance using the specified properties.
     * @function create
     * @memberof Msg
     * @static
     * @param {IMsg=} [properties] Properties to set
     * @returns {Msg} Msg instance
     */
    Msg.create = function create(properties) {
        return new Msg(properties);
    };

    /**
     * Encodes the specified Msg message. Does not implicitly {@link Msg.verify|verify} messages.
     * @function encode
     * @memberof Msg
     * @static
     * @param {IMsg} message Msg message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Msg.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.head != null && message.hasOwnProperty("head"))
            $root.Head.encode(message.head, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.body != null && message.hasOwnProperty("body"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.body);
        return writer;
    };

    /**
     * Encodes the specified Msg message, length delimited. Does not implicitly {@link Msg.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Msg
     * @static
     * @param {IMsg} message Msg message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Msg.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Msg message from the specified reader or buffer.
     * @function decode
     * @memberof Msg
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Msg} Msg
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Msg.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Msg();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.head = $root.Head.decode(reader, reader.uint32());
                break;
            case 2:
                message.body = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Msg message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Msg
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Msg} Msg
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Msg.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Msg message.
     * @function verify
     * @memberof Msg
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Msg.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.head != null && message.hasOwnProperty("head")) {
            var error = $root.Head.verify(message.head);
            if (error)
                return "head." + error;
        }
        if (message.body != null && message.hasOwnProperty("body"))
            if (!$util.isString(message.body))
                return "body: string expected";
        return null;
    };

    /**
     * Creates a Msg message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Msg
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Msg} Msg
     */
    Msg.fromObject = function fromObject(object) {
        if (object instanceof $root.Msg)
            return object;
        var message = new $root.Msg();
        if (object.head != null) {
            if (typeof object.head !== "object")
                throw TypeError(".Msg.head: object expected");
            message.head = $root.Head.fromObject(object.head);
        }
        if (object.body != null)
            message.body = String(object.body);
        return message;
    };

    /**
     * Creates a plain object from a Msg message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Msg
     * @static
     * @param {Msg} message Msg
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Msg.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.head = null;
            object.body = "";
        }
        if (message.head != null && message.hasOwnProperty("head"))
            object.head = $root.Head.toObject(message.head, options);
        if (message.body != null && message.hasOwnProperty("body"))
            object.body = message.body;
        return object;
    };

    /**
     * Converts this Msg to JSON.
     * @function toJSON
     * @memberof Msg
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Msg.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Msg;
})();

$root.Head = (function() {

    /**
     * Properties of a Head.
     * @exports IHead
     * @interface IHead
     * @property {string|null} [msgId] Head msgId
     * @property {number|null} [msgType] Head msgType
     * @property {number|null} [msgContentType] Head msgContentType
     * @property {string|null} [fromId] Head fromId
     * @property {string|null} [toId] Head toId
     * @property {number|Long|null} [timestamp] Head timestamp
     * @property {number|null} [statusReport] Head statusReport
     * @property {string|null} [extend] Head extend
     */

    /**
     * Constructs a new Head.
     * @exports Head
     * @classdesc Represents a Head.
     * @implements IHead
     * @constructor
     * @param {IHead=} [properties] Properties to set
     */
    function Head(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Head msgId.
     * @member {string} msgId
     * @memberof Head
     * @instance
     */
    Head.prototype.msgId = "";

    /**
     * Head msgType.
     * @member {number} msgType
     * @memberof Head
     * @instance
     */
    Head.prototype.msgType = 0;

    /**
     * Head msgContentType.
     * @member {number} msgContentType
     * @memberof Head
     * @instance
     */
    Head.prototype.msgContentType = 0;

    /**
     * Head fromId.
     * @member {string} fromId
     * @memberof Head
     * @instance
     */
    Head.prototype.fromId = "";

    /**
     * Head toId.
     * @member {string} toId
     * @memberof Head
     * @instance
     */
    Head.prototype.toId = "";

    /**
     * Head timestamp.
     * @member {number|Long} timestamp
     * @memberof Head
     * @instance
     */
    Head.prototype.timestamp = $util.Long ? $util.Long.fromBits(0,0,false) : 0;

    /**
     * Head statusReport.
     * @member {number} statusReport
     * @memberof Head
     * @instance
     */
    Head.prototype.statusReport = 0;

    /**
     * Head extend.
     * @member {string} extend
     * @memberof Head
     * @instance
     */
    Head.prototype.extend = "";

    /**
     * Creates a new Head instance using the specified properties.
     * @function create
     * @memberof Head
     * @static
     * @param {IHead=} [properties] Properties to set
     * @returns {Head} Head instance
     */
    Head.create = function create(properties) {
        return new Head(properties);
    };

    /**
     * Encodes the specified Head message. Does not implicitly {@link Head.verify|verify} messages.
     * @function encode
     * @memberof Head
     * @static
     * @param {IHead} message Head message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Head.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.msgId != null && message.hasOwnProperty("msgId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.msgId);
        if (message.msgType != null && message.hasOwnProperty("msgType"))
            writer.uint32(/* id 2, wireType 0 =*/16).int32(message.msgType);
        if (message.msgContentType != null && message.hasOwnProperty("msgContentType"))
            writer.uint32(/* id 3, wireType 0 =*/24).int32(message.msgContentType);
        if (message.fromId != null && message.hasOwnProperty("fromId"))
            writer.uint32(/* id 4, wireType 2 =*/34).string(message.fromId);
        if (message.toId != null && message.hasOwnProperty("toId"))
            writer.uint32(/* id 5, wireType 2 =*/42).string(message.toId);
        if (message.timestamp != null && message.hasOwnProperty("timestamp"))
            writer.uint32(/* id 6, wireType 0 =*/48).int64(message.timestamp);
        if (message.statusReport != null && message.hasOwnProperty("statusReport"))
            writer.uint32(/* id 7, wireType 0 =*/56).int32(message.statusReport);
        if (message.extend != null && message.hasOwnProperty("extend"))
            writer.uint32(/* id 8, wireType 2 =*/66).string(message.extend);
        return writer;
    };

    /**
     * Encodes the specified Head message, length delimited. Does not implicitly {@link Head.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Head
     * @static
     * @param {IHead} message Head message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Head.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Head message from the specified reader or buffer.
     * @function decode
     * @memberof Head
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Head} Head
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Head.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Head();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.msgId = reader.string();
                break;
            case 2:
                message.msgType = reader.int32();
                break;
            case 3:
                message.msgContentType = reader.int32();
                break;
            case 4:
                message.fromId = reader.string();
                break;
            case 5:
                message.toId = reader.string();
                break;
            case 6:
                message.timestamp = reader.int64();
                break;
            case 7:
                message.statusReport = reader.int32();
                break;
            case 8:
                message.extend = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Head message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Head
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Head} Head
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Head.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Head message.
     * @function verify
     * @memberof Head
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Head.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.msgId != null && message.hasOwnProperty("msgId"))
            if (!$util.isString(message.msgId))
                return "msgId: string expected";
        if (message.msgType != null && message.hasOwnProperty("msgType"))
            if (!$util.isInteger(message.msgType))
                return "msgType: integer expected";
        if (message.msgContentType != null && message.hasOwnProperty("msgContentType"))
            if (!$util.isInteger(message.msgContentType))
                return "msgContentType: integer expected";
        if (message.fromId != null && message.hasOwnProperty("fromId"))
            if (!$util.isString(message.fromId))
                return "fromId: string expected";
        if (message.toId != null && message.hasOwnProperty("toId"))
            if (!$util.isString(message.toId))
                return "toId: string expected";
        if (message.timestamp != null && message.hasOwnProperty("timestamp"))
            if (!$util.isInteger(message.timestamp) && !(message.timestamp && $util.isInteger(message.timestamp.low) && $util.isInteger(message.timestamp.high)))
                return "timestamp: integer|Long expected";
        if (message.statusReport != null && message.hasOwnProperty("statusReport"))
            if (!$util.isInteger(message.statusReport))
                return "statusReport: integer expected";
        if (message.extend != null && message.hasOwnProperty("extend"))
            if (!$util.isString(message.extend))
                return "extend: string expected";
        return null;
    };

    /**
     * Creates a Head message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Head
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Head} Head
     */
    Head.fromObject = function fromObject(object) {
        if (object instanceof $root.Head)
            return object;
        var message = new $root.Head();
        if (object.msgId != null)
            message.msgId = String(object.msgId);
        if (object.msgType != null)
            message.msgType = object.msgType | 0;
        if (object.msgContentType != null)
            message.msgContentType = object.msgContentType | 0;
        if (object.fromId != null)
            message.fromId = String(object.fromId);
        if (object.toId != null)
            message.toId = String(object.toId);
        if (object.timestamp != null)
            if ($util.Long)
                (message.timestamp = $util.Long.fromValue(object.timestamp)).unsigned = false;
            else if (typeof object.timestamp === "string")
                message.timestamp = parseInt(object.timestamp, 10);
            else if (typeof object.timestamp === "number")
                message.timestamp = object.timestamp;
            else if (typeof object.timestamp === "object")
                message.timestamp = new $util.LongBits(object.timestamp.low >>> 0, object.timestamp.high >>> 0).toNumber();
        if (object.statusReport != null)
            message.statusReport = object.statusReport | 0;
        if (object.extend != null)
            message.extend = String(object.extend);
        return message;
    };

    /**
     * Creates a plain object from a Head message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Head
     * @static
     * @param {Head} message Head
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Head.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.msgId = "";
            object.msgType = 0;
            object.msgContentType = 0;
            object.fromId = "";
            object.toId = "";
            if ($util.Long) {
                var long = new $util.Long(0, 0, false);
                object.timestamp = options.longs === String ? long.toString() : options.longs === Number ? long.toNumber() : long;
            } else
                object.timestamp = options.longs === String ? "0" : 0;
            object.statusReport = 0;
            object.extend = "";
        }
        if (message.msgId != null && message.hasOwnProperty("msgId"))
            object.msgId = message.msgId;
        if (message.msgType != null && message.hasOwnProperty("msgType"))
            object.msgType = message.msgType;
        if (message.msgContentType != null && message.hasOwnProperty("msgContentType"))
            object.msgContentType = message.msgContentType;
        if (message.fromId != null && message.hasOwnProperty("fromId"))
            object.fromId = message.fromId;
        if (message.toId != null && message.hasOwnProperty("toId"))
            object.toId = message.toId;
        if (message.timestamp != null && message.hasOwnProperty("timestamp"))
            if (typeof message.timestamp === "number")
                object.timestamp = options.longs === String ? String(message.timestamp) : message.timestamp;
            else
                object.timestamp = options.longs === String ? $util.Long.prototype.toString.call(message.timestamp) : options.longs === Number ? new $util.LongBits(message.timestamp.low >>> 0, message.timestamp.high >>> 0).toNumber() : message.timestamp;
        if (message.statusReport != null && message.hasOwnProperty("statusReport"))
            object.statusReport = message.statusReport;
        if (message.extend != null && message.hasOwnProperty("extend"))
            object.extend = message.extend;
        return object;
    };

    /**
     * Converts this Head to JSON.
     * @function toJSON
     * @memberof Head
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Head.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Head;
})();

module.exports = $root;
